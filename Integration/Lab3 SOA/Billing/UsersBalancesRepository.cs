using System.Collections.Concurrent;

namespace Billing;

public sealed class UsersBalancesRepository : IUsersBalancesRepository
{
    public UsersBalancesRepository()
    {
        _balances = [];

        _balances[1] = 1000;
    }

    private ConcurrentDictionary<long, decimal> _balances;

    public bool CheckBalanceAmountExistense(long userId, decimal amount)
    {
        ArgumentOutOfRangeException.ThrowIfLessThanOrEqual(amount, 0);

        return _balances.TryGetValue(userId, out var balance) 
            && balance >= amount;
    }

    public void UpdateUserBalance(long userId, decimal amount)
    {
        ArgumentOutOfRangeException.ThrowIfLessThanOrEqual(amount, 0);

        if (_balances.TryGetValue(userId, out var balance))
        {
            balance -= amount;
        }
    }
}