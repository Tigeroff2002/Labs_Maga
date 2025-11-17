using System.Collections.Concurrent;

using CRM.Models;

namespace CRM;

public sealed class InvoiceRepository : IInvoiceRepository
{
    public long AddAndReturnId(NewInvoiceRequestCommand command)
    {
        ArgumentNullException.ThrowIfNull(command);

        var newId = _invoices.Count + 1;

        _invoices[newId] = command;

        return newId;
    }

    private ConcurrentDictionary<long, NewInvoiceRequestCommand> _invoices = [];
}