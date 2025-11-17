using System.Xml.Serialization;

namespace ESB_Mapper.Models;

[Serializable]
[XmlRoot("command_result")]
public class XmlCommandResultResponse
{
    [XmlElement(ElementName = "is_success")]
    public required bool IsSuccess { get; init; }

    [XmlElement(ElementName = "failure_message")]
    public string? FailureMessage { get; init; }
}