using System.ComponentModel.DataAnnotations;
using System.Diagnostics.CodeAnalysis;
using System.Text;
using CRM.Models;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;

namespace CRM;

[ApiController]
[Route("crm")]
[Produces("application/json")]
public sealed class Controller : ControllerBase
{
    public Controller(
        IUserIdentityFacade facade,
        IInvoiceRepository invoiceRepository,
        ILogger<Controller> logger,
        INotificationHandler notificationHandler)
    {
        _userIdentityFacade = facade
            ?? throw new ArgumentNullException(nameof(facade));
        _invoiceRepository = invoiceRepository
            ?? throw new ArgumentNullException(nameof(invoiceRepository));
        _logger = logger ?? throw new ArgumentNullException(nameof(logger));
        _notificationHandler = notificationHandler
            ?? throw new ArgumentNullException(nameof(notificationHandler));
    }

    [HttpPost]
    [Route("create")]
    public async Task<IActionResult> CreateNewInvoiceAsync(
        [Required][NotNull] AddNewInvoiceRequest request,
        CancellationToken token)
    {
        ArgumentNullException.ThrowIfNull(request);

        var userId = _userIdentityFacade.UserId;

        using var httpClient = new HttpClient();

        var command = new NewInvoiceRequestCommand
        {
            Amount = request.Amount,
            TraceId = request.TraceId,
            InvoiceId = default,
            UserId = userId,
        };

        var createdId = _invoiceRepository.AddAndReturnId(command);

        command.InvoiceId = createdId;

        using var stringWriter = new StringWriter();

        using var writer = new JsonTextWriter(stringWriter);

        _serializer.Serialize(writer, command);

        _logger.LogInformation(
            "Posting http request to ESB for applying new invoice {Id}",
            createdId);

        Console.WriteLine($"Json content: {stringWriter}");

        var response = await httpClient.PostAsync(
            "https://localhost:7021/esb/create",
            new StringContent(stringWriter.ToString(), Encoding.UTF8, "application/json"));

        var stringContent = await response.Content.ReadAsStringAsync();

        Console.WriteLine($"Received json content result from ESB: {stringContent}");

        var resultResponse = JsonConvert.DeserializeObject<CommandResultResponse>(
            stringContent);

        if (resultResponse!.IsSuccess)
        {
            _logger.LogInformation(
                "Successfully created and applied invoice {Id} for user {UserId}", 
                createdId, 
                userId);

            _notificationHandler.HandleNotification(new()
            {
                UserId = userId,
                Amount = command.Amount,
                InvoiceId = command.InvoiceId,
                UserEmail = "email"
                // может быть получена с какого то хранилища (либо самим Notifications сервисом)
            });

            return Ok(resultResponse);
        }

        _logger.LogWarning(
            "Failed to applied invoice {Id} for user {UserId}, Reason: '{Message}'",
            createdId,
            userId,
            resultResponse.FailureMessage);

        return BadRequest(response);
    }

    private readonly IUserIdentityFacade _userIdentityFacade;
    private readonly IInvoiceRepository _invoiceRepository;
    private readonly ILogger<Controller> _logger;
    private readonly INotificationHandler _notificationHandler;

    private static readonly JsonSerializer _serializer = JsonSerializer.CreateDefault();
}