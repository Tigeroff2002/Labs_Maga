using System.Xml.Serialization;

namespace Billing.Models;

[Serializable]
[XmlRoot("command_result")]
public sealed class CommandResultResponse
{
    [XmlElement(ElementName = "is_success")]
    public required bool IsSuccess { get; init; }

    [XmlElement(ElementName = "failure_message")]
    public string? FailureMessage { get; init; }
}