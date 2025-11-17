using Billing.Models;
using Microsoft.AspNetCore.Mvc;
using System.ComponentModel.DataAnnotations;
using System.Diagnostics.CodeAnalysis;

namespace Billing;

[ApiController]
[Route("billing")]
[Produces("application/xml")]
public sealed class Controller : ControllerBase
{
    public Controller(
        ILogger<Controller> logger,
        IUsersBalancesRepository repository)
    {
        _logger = logger ?? throw new ArgumentNullException(nameof(logger));
        _repository = repository ?? throw new ArgumentNullException(nameof(repository));
    }

    [HttpPost]
    [Route("create")]
    public async Task<IActionResult> ApplyInvoiceAndSynchronizeBalanceAsync(
        [Required][NotNull] InvoiceCommand command,
        CancellationToken token)
    {
        ArgumentNullException.ThrowIfNull(command);

        var isSuccess = false;
        string? failureMessage = null;

        if (_repository.CheckBalanceAmountExistense(command.UserId, command.Amount))
        {
            _repository.UpdateUserBalance(command.UserId, command.Amount);

            isSuccess = true;
        }
        else
        {
            _logger.LogWarning(
                "Failed to synchronize balance for user {UserId} on invoice {Id}", 
                command.UserId,
                command.InvoiceId);

            failureMessage = $"Failed to synchronize balance for user {command.UserId}";
        }

        var resultDto = new CommandResultResponse
        {
            IsSuccess = isSuccess,
            FailureMessage = failureMessage
        };

        return Ok(resultDto);
    }

    private readonly ILogger<Controller> _logger;
    private readonly IUsersBalancesRepository _repository;
}