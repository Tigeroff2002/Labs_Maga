using System.Text.Json;
using Confluent.Kafka;

namespace NotificationsHandler;

public sealed class BrokerNotificationsProcessor :
    IBrokerNotificationsProcessor,
    IDisposable
{
    public BrokerNotificationsProcessor(
        ILogger<BrokerNotificationsProcessor> logger)
    {
        _logger = logger ?? throw new ArgumentNullException(nameof(logger));

        var config = new ConsumerConfig
        {
            BootstrapServers = "127.0.0.1:9092",
            GroupId = "invoices-group",
            AutoOffsetReset = AutoOffsetReset.Earliest,
            EnableAutoOffsetStore = false
        };

        _consumer = new ConsumerBuilder<Ignore, string>(config)
            .SetErrorHandler((_, e) => _logger.LogError($"Kafka Error: {e.Reason}"))
            .SetLogHandler((_, logMessage) =>
                _logger.LogInformation($"Kafka Log: {logMessage.Message}"))
            .Build();

        _consumer.Subscribe("notifications");
    }

    public async Task ProcessAsync(CancellationToken token)
    {
        _logger.LogInformation("Starting Kafka consumer...");

        try
        {
            while (!token.IsCancellationRequested)
            {
                try
                {
                    var consumeResult = _consumer.Consume(token);

                    if (consumeResult?.Message?.Value != null)
                    {
                        await ProcessMessageAsync(consumeResult.Message.Value, token);

                        _consumer.StoreOffset(consumeResult);
                    }
                }
                catch (ConsumeException ex)
                {
                    _logger.LogError(ex, $"Error consuming message: {ex.Error.Reason}");
                }
                catch (OperationCanceledException)
                {
                    _logger.LogInformation("Consumer operation was cancelled");

                    break;
                }
                catch (Exception ex)
                {
                    _logger.LogError(ex, "Unexpected error occurred while consuming message");
                }
            }
        }
        finally
        {
            _logger.LogInformation("Stopping Kafka consumer...");
        }
    }

    private async Task ProcessMessageAsync(string jsonMessage, CancellationToken token)
    {
        try
        {
            using var document = JsonDocument.Parse(jsonMessage);

            _logger.LogInformation("Received JSON message: {Message}", jsonMessage);

            // тут можно отправить на почту

            await Task.CompletedTask;
        }
        catch (JsonException ex)
        {
            _logger.LogError(ex, "Invalid JSON message received: {Message}", jsonMessage);
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Error processing message: {Message}", jsonMessage);
        }
    }

    public void Dispose()
    {
        if (!_disposed)
        {
            _consumer?.Close();
            _consumer?.Dispose();

            _disposed = true;
        }
    }

    private readonly IConsumer<Ignore, string> _consumer;
    private readonly ILogger _logger;

    private bool _disposed = false;
}