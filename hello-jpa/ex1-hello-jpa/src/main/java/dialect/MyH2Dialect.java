package dialect;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

// Hibernate 5 버전
//public class MyH2Dialect extends H2Dialect {
//
//    public MyH2Dialect() {
//        registerFunction("group_concat", new StandardSQLFunction("group_concat", StandardBasicTypes.STRING));
//    }
//}

// Hibernate 6 버전
public class MyH2Dialect implements FunctionContributor {
    @Override
    public void contributeFunctions(FunctionContributions functionContributions) {
        functionContributions.getFunctionRegistry().registerNamed(
                "group_concat",
                functionContributions
                        .getTypeConfiguration()
                        .getBasicTypeRegistry()
                        .resolve(StandardBasicTypes.STRING)
        );
    }
}