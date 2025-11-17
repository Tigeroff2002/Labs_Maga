using Newtonsoft.Json;

namespace CRM.Models;

public sealed class AddNewInvoiceRequest
{
    [JsonProperty("amount", Required = Required.Always)]
    public required decimal Amount { get; init; }

    [JsonProperty("trace_id", Required = Required.Always)]
    public required string TraceId { get; init; }
}