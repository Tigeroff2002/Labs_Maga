using Newtonsoft.Json.Converters;
using Newtonsoft.Json.Serialization;

var builder = WebApplication.CreateBuilder(args);

builder.Services
    .AddControllers()
    .AddNewtonsoftJson(
        opt => opt.SerializerSettings.Converters.Add(
            new StringEnumConverter(new SnakeCaseNamingStrategy())));

builder.Services.AddHealthChecks();

builder.Services
    .AddCors(options => options
        .AddDefaultPolicy(builder => _ = builder
            .SetIsOriginAllowed(_ => true)
            .AllowAnyMethod()
            .AllowAnyHeader()
            .AllowCredentials()));

builder.Services.AddLogging();

var app = builder.Build();

app.UseRouting();
app.UseHttpsRedirection();
app.MapControllers();
app.UseHealthChecks("/hc");

app.Run();