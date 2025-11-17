using Confluent.Kafka;
using CRM.Models;
using Newtonsoft.Json;

namespace CRM;

public sealed class NotificationHandler : INotificationHandler
{
    public NotificationHandler(ILogger<NotificationHandler> logger)
    {
        _logger = logger ?? throw new ArgumentNullException(nameof(logger));

       _producer = new ProducerBuilder<long, string>(
            new ProducerConfig
            {
                BootstrapServers = "127.0.0.1:9092"
            })
            .Build();
    }

    public void HandleNotification(NotificationMessage message)
    {
        ArgumentNullException.ThrowIfNull(message);

        using var stringWriter = new StringWriter();

        using var writer = new JsonTextWriter(stringWriter);

        _serializer.Serialize(writer, message);

        var json = stringWriter.ToString();

        _logger.LogInformation(
            "Producing notification message {Message}" +
            " for Notifications system for user {UserId}",
            json,
            message.UserId);

        _producer.Produce(
            topic: "notifications", 
            new Message<long, string>
            {
                Key = message.UserId,
                Value = json
            });
    }

    private readonly IProducer<long, string> _producer;
    private readonly ILogger _logger;

    private static readonly JsonSerializer _serializer = JsonSerializer.CreateDefault();
}