using Newtonsoft.Json;

namespace ESB_Mapper.Models;

public sealed class JsonCommandResultResponse
{
    [JsonProperty("is_success", Required = Required.Always)]
    public required bool IsSuccess { get; init; }

    [JsonProperty("failure_message")]
    public string? FailureMessage { get; init; }
}