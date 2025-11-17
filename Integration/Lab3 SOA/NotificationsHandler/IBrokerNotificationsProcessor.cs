namespace NotificationsHandler;

public interface IBrokerNotificationsProcessor
{
    Task ProcessAsync(CancellationToken token);
}
