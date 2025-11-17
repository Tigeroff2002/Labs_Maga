using System.Xml.Serialization;

namespace Billing.Models;

public sealed class CommandResultResponse
{
    [XmlElement(ElementName = "is_success")]
    public required bool IsSuccess { get; init; }

    [XmlElement(ElementName = "failure_message")]
    public string? FailureMessage { get; init; }
}