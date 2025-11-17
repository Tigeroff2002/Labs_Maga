using Newtonsoft.Json;

namespace CRM.Models;

public sealed class NotificationMessage
{
    [JsonProperty("amount", Required = Required.Always)]
    public required decimal Amount { get; init; }

    [JsonProperty("invoice_id", Required = Required.Always)]
    public required long InvoiceId { get; set; }

    [JsonProperty("user_id", Required = Required.Always)]
    public required long UserId { get; init; }

    [JsonProperty("user_email", Required = Required.Always)]
    public required string UserEmail { get; init; }
}