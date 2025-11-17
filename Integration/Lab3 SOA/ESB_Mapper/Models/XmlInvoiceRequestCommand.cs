using System.Xml.Serialization;
using Newtonsoft.Json;

namespace ESB_Mapper.Models;

public class XmlInvoiceRequestCommand
{
    [XmlElement(ElementName = "amount")]
    public required decimal Amount { get; init; }

    [XmlElement(ElementName = "trace_id")]
    public required string TraceId { get; init; }

    [XmlElement(ElementName = "invoice_id")]
    public required long InvoiceId { get; set; }

    [XmlElement(ElementName = "user_id")]
    public required long UserId { get; init; }
}