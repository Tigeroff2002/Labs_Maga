namespace NotificationsHandler;

public class BrokerBackgroundService : BackgroundService
{
    public BrokerBackgroundService(IBrokerNotificationsProcessor processor)
    {
        _processor = processor ?? throw new ArgumentNullException(nameof(processor));
    }

    /// <inheritdoc/>
    protected override Task ExecuteAsync(CancellationToken stoppingToken) =>
        Task.Run(
            async () => await _processor.ProcessAsync(stoppingToken),
            stoppingToken);

    private readonly IBrokerNotificationsProcessor _processor;
}