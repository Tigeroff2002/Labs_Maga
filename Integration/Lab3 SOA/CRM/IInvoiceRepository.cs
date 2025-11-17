using CRM.Models;

namespace CRM;

public interface IInvoiceRepository
{
    long AddAndReturnId(NewInvoiceRequestCommand command);
}