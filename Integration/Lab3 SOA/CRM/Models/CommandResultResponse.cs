using Newtonsoft.Json;

namespace CRM.Models;

public sealed class CommandResultResponse
{
    [JsonProperty("is_success", Required = Required.Always)]
    public required bool IsSuccess { get; init; }

    [JsonProperty("failure_message")]
    public string? FailureMessage { get; init; }
}