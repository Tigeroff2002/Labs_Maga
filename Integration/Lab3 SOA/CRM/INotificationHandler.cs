using CRM.Models;

namespace CRM;

public interface INotificationHandler
{
    void HandleNotification(NotificationMessage message);
}