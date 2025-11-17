using Billing;

var builder = WebApplication.CreateBuilder(args);

builder.Services
    .AddControllers()
    .AddXmlSerializerFormatters();

builder.Services.AddHealthChecks();

builder.Services
    .AddCors(options => options
        .AddDefaultPolicy(builder => _ = builder
            .SetIsOriginAllowed(_ => true)
            .AllowAnyMethod()
            .AllowAnyHeader()
            .AllowCredentials()));

builder.Services
    .AddLogging()
    .AddSingleton<IUsersBalancesRepository, UsersBalancesRepository>();

var app = builder.Build();

app.UseRouting();
app.UseHttpsRedirection();
app.MapControllers();
app.UseHealthChecks("/hc");

app.Run();