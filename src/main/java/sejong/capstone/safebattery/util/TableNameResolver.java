package sejong.capstone.safebattery.util;

import jakarta.persistence.Table;
import org.springframework.stereotype.Component;

@Component
public class TableNameResolver {
    /**
     * @param domainClass JPA 엔티티 클래스
     * @return @Table(name=...) 이 붙어 있으면 그 값, 아니면 클래스명 → snake_case 변환
     */
    public static String resolve(Class<?> domainClass) {
        Table table = domainClass.getAnnotation(Table.class);
        if (table != null && !table.name().isBlank()) {
            return table.name();
        }
        return toSnakeCase(domainClass.getSimpleName());
    }

    private static String toSnakeCase(String str) {
        return str.replaceAll("([a-z])([A-Z])", "$1_$2")
            .toLowerCase();
    }
}
