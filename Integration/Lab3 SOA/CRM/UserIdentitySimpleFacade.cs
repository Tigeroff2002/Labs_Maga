using System.Security.Claims;

namespace CRM;

public sealed class UserIdentitySimpleFacade : IUserIdentityFacade
{
    public UserIdentitySimpleFacade(IHttpContextAccessor contextAccessor)
    {
        ArgumentNullException.ThrowIfNull(contextAccessor);

        _contextAccessor = contextAccessor;
    }

    public long UserId => _contextAccessor.HttpContext?.User.FindFirstValue("user_id") is { } value
        ? long.Parse(value)
        : 1;

    private readonly IHttpContextAccessor _contextAccessor;
}