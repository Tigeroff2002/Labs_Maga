using Newtonsoft.Json;

namespace CRM.Models;

public class NewInvoiceRequestCommand
{
    [JsonProperty("amount", Required = Required.Always)]
    public required decimal Amount { get; init; }

    [JsonProperty("trace_id", Required = Required.Always)]
    public required string TraceId { get; init; }

    [JsonProperty("invoice_id", Required = Required.Always)]
    public required long InvoiceId { get; set; }

    [JsonProperty("user_id", Required = Required.Always)]
    public required long UserId { get; init; }
}