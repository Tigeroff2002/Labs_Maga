using ESB_Mapper.Models;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using System.ComponentModel.DataAnnotations;
using System.Diagnostics.CodeAnalysis;
using System.Text;
using System.Xml.Serialization;

namespace ESB_Mapper;

[ApiController]
[Route("esb")]
[Produces("application/json")]
public sealed class Controller : ControllerBase
{
    public Controller(
        ILogger<Controller> logger)
    {
        _logger = logger ?? throw new ArgumentNullException(nameof(logger));
    }

    [HttpPost]
    [Route("create")]
    public async Task<IActionResult> CreateProxyNewInvoiceAsync(
        [Required][NotNull] JsonInvoiceRequestCommand jsonCommand,
        CancellationToken token)
    {
        ArgumentNullException.ThrowIfNull(jsonCommand);

        using var httpClient = new HttpClient();

        var xmlCommand = new XmlInvoiceRequestCommand
        {
            Amount = jsonCommand.Amount,
            TraceId = jsonCommand.TraceId,
            InvoiceId = jsonCommand.InvoiceId,
            UserId = jsonCommand.UserId
        };

        var xmlSerializer = new XmlSerializer(typeof(XmlInvoiceRequestCommand));

        using var stringWriter = new StringWriterWithEncoding(Encoding.UTF8);

        xmlSerializer.Serialize(stringWriter, xmlCommand);

        _logger.LogInformation(
            "Posting proxy http request for billing for applying new invoice {Id}",
            xmlCommand.InvoiceId);

        Console.WriteLine($"Sending XML: {stringWriter}");

        var response = await httpClient.PostAsync(
            "https://localhost:7084/billing/create",
            new StringContent(stringWriter.ToString(), Encoding.UTF8, "application/xml"));

        var xmlDeserializer = new XmlSerializer(typeof(XmlCommandResultResponse));

        var stringContent = await response.Content.ReadAsStringAsync();

        Console.WriteLine($"Received xml response from billing: {stringWriter}");

        using var stringReader = new StringReader(stringContent);

        var resultResponse = 
            (XmlCommandResultResponse)xmlDeserializer.Deserialize(stringReader)!;

        var jsonResponse = new JsonCommandResultResponse
        {
            IsSuccess = resultResponse.IsSuccess,
            FailureMessage = resultResponse.FailureMessage
        };

        using var jsonStringWriter = new StringWriter();

        using var writer = new JsonTextWriter(jsonStringWriter);

        _serializer.Serialize(writer, jsonResponse);

        if (resultResponse!.IsSuccess)
        {
            _logger.LogInformation(
                "Successfully created and applied invoice {Id} for user {UserId}",
                xmlCommand.InvoiceId,
                xmlCommand.UserId);

            return Ok(jsonResponse);
        }

        _logger.LogWarning(
            "Failed to applied invoice {Id} for user {UserId}, Reason: '{Message}'",
            xmlCommand.InvoiceId,
            xmlCommand.UserId,
            resultResponse.FailureMessage);

        return BadRequest(jsonResponse);
    }

    private readonly ILogger<Controller> _logger;

    private static readonly JsonSerializer _serializer = JsonSerializer.CreateDefault();

    private class StringWriterWithEncoding : StringWriter
    {
        private readonly Encoding _encoding;

        public StringWriterWithEncoding(Encoding encoding)
        {
            _encoding = encoding;
        }

        public override Encoding Encoding => _encoding;
    }
}