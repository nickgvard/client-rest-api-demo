package my.education.iexcloudapidemo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


@SpringBootApplication
public class IexCloudApiDemoApplication implements CommandLineRunner {

//    private final GetAllCompanyRestClientCommand getAllCompanyRestClientCommand;
//    private final GetCompanyStockRestClientCommand getCompanyStockRestClientCommand;
//    private final RestClientInvoker restClientInvoker;
//
//    public IexCloudApiDemoApplication(@Lazy GetAllCompanyRestClientCommand getAllCompanyRestClientCommand,
//                                      @Lazy GetCompanyStockRestClientCommand getCompanyStockRestClientCommand,
//                                      RestClientInvoker restClientInvoker) {
//        this.getAllCompanyRestClientCommand = getAllCompanyRestClientCommand;
//        this.getCompanyStockRestClientCommand = getCompanyStockRestClientCommand;
//        this.restClientInvoker = restClientInvoker;
//    }

    public static void main(String[] args) {
        SpringApplication.run(IexCloudApiDemoApplication.class, args);
        new SpringApplicationBuilder(IexCloudApiDemoApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Override
    public void run(String... args) throws Exception {
//        restClientInvoker.setRestClientCommand(getAllCompanyRestClientCommand);
//
//        CompletableFuture<?> future = restClientInvoker.executeAsyncRequest();
//
//        if(future.isDone()) {
//            List<CompanyDto> o = (List<CompanyDto>) future.get();
//            restClientInvoker.setRestClientCommand(getCompanyStockRestClientCommand);
//
//            for (CompanyDto companyDto : o) {
//                getCompanyStockRestClientCommand.setCompany(companyDto);
//                restClientInvoker.executeAsyncRequest();
//            }
//        }
    }
}
