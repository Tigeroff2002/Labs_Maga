using NotificationsHandler;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddHealthChecks();

builder.Services
    .AddLogging()
    .AddHostedService<BrokerBackgroundService>()
    .AddSingleton<IBrokerNotificationsProcessor, BrokerNotificationsProcessor>();

var app = builder.Build();

app.UseHealthChecks("/hc");

app.Run();