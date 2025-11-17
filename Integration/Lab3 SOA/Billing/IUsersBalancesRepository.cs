namespace Billing;

public interface IUsersBalancesRepository
{
    bool CheckBalanceAmountExistense(long userId, decimal amount);

    void UpdateUserBalance(long userId, decimal amount);
}
