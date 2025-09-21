package com.example.doctorcare.core.processor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.lang.model.element.Modifier;

import org.springframework.javapoet.ClassName;
import org.springframework.javapoet.CodeBlock;
import org.springframework.javapoet.FieldSpec;
import org.springframework.javapoet.JavaFile;
import org.springframework.javapoet.MethodSpec;
import org.springframework.javapoet.TypeSpec;

import com.example.doctorcare.core.enums_NotUsedYet.Action;
import com.example.doctorcare.core.enums_NotUsedYet.Resource;
import com.example.doctorcare.core.enums_NotUsedYet.Scope;
import com.example.doctorcare.core.security.PermissionHelper;



/**
 * Lớp tiện ích để sinh tự động file {@code Permissions.java} từ enum {@link Resource}.
 * <p>
 * Lớp này được tạo ra để thay thế cho {@code PermissionConstantProcessor} khi việc
 * thực thi annotation processor gặp vấn đề. Chạy phương thức {@code main} của lớp này
 * để tạo file hằng số quyền.
 */
public class PermissionClassGenerator {

    private static final String TARGET_PACKAGE = "vn.look.core.security";
    private static final String TARGET_CLASS_NAME = "Permissions";
    // Giả định rằng script được chạy từ thư mục gốc của project.
    private static final String SOURCE_ROOT_PATH = "src/main/java";
    private static final String JAVA_SOURCE_ROOT_PATH = "src/main/java";
    // Đường dẫn cho file TypeScript được sinh ra. Bạn có thể thay đổi nếu cần.
    private static final String TS_TARGET_DIR = "frontend/generated";
    private static final String TS_TARGET_FILENAME = "permissions.ts";

    public static void main(String[] args) {
        try {
            System.out.println("Bắt đầu sinh file hằng số quyền...");
            System.out.println("Bắt đầu sinh file hằng số quyền cho Java...");
            generatePermissionsClass();
            System.out.println("Đã sinh thành công file: " + SOURCE_ROOT_PATH + "/" + TARGET_PACKAGE.replace('.', '/') + "/" + TARGET_CLASS_NAME + ".java");
            System.out.println("Đã sinh thành công file Java: " + JAVA_SOURCE_ROOT_PATH + "/" + TARGET_PACKAGE.replace('.', '/') + "/" + TARGET_CLASS_NAME + ".java");

            System.out.println("Bắt đầu sinh file hằng số quyền cho TypeScript...");
            generatePermissionsTypeScript();
            System.out.println("Đã sinh thành công file TypeScript: " + TS_TARGET_DIR + "/" + TS_TARGET_FILENAME);

        } catch (IOException e) {
            System.err.println("Lỗi trong quá trình sinh file:");
            e.printStackTrace();
        }
    }

    private static void generatePermissionsClass() throws IOException {
        // Định nghĩa các lớp cần import
        ClassName permissionHelperClass = ClassName.get(TARGET_PACKAGE, "PermissionHelper");
        ClassName actionEnum = ClassName.get(Action.class);
        ClassName resourceEnum = ClassName.get(Resource.class);
        ClassName scopeEnum = ClassName.get(Scope.class);

        // Bắt đầu xây dựng lớp Permissions
        TypeSpec.Builder permissionsClassBuilder = TypeSpec.classBuilder(TARGET_CLASS_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addJavadoc(buildClassJavadoc());

        // Thêm constructor private
        permissionsClassBuilder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addComment("Lớp tiện ích, không cho phép khởi tạo")
                .build());

        // Lặp qua từng hằng số trong enum Resource (ví dụ: ENTERPRISE, USER, ...)
        for (Resource resource : Resource.values()) {
            String resourceName = resource.name();
            String nestedClassName = toPascalCase(resourceName);

            // Bắt đầu xây dựng lớp tĩnh lồng nhau (ví dụ: public static final class Enterprise)
            TypeSpec.Builder nestedClassBuilder = TypeSpec.classBuilder(nestedClassName)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .addJavadoc("Các quyền liên quan đến tài nguyên: {@code $L}", resourceName)
                    .addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build());

            // Lấy danh sách Action và Scope hợp lệ trực tiếp từ các phương thức của enum.
            // Cách này đáng tin cậy hơn là dùng reflection để đọc annotation,
            // vì nó sử dụng cùng một "nguồn chân lý" (source of truth) với phần còn lại của ứng dụng.
            List<String> actions = resource.getValidActions().stream().map(Enum::name).collect(Collectors.toList());
            actions.sort(String::compareTo); // Sắp xếp để output nhất quán
            List<String> scopes = resource.getValidScopes().stream().map(Enum::name).collect(Collectors.toList());
            scopes.sort(String::compareTo); // Sắp xếp để output nhất quán

            // Tạo các hằng số quyền
            for (String action : actions) {
                for (String scope : scopes) {
                    String fieldName = action.toUpperCase() + "_" + scope.toUpperCase();
                    FieldSpec field = FieldSpec.builder(String.class, fieldName, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                            .initializer(CodeBlock.of("$T.buildPermissionKey($T.$L, $T.$L, $T.$L)",
                                    permissionHelperClass, actionEnum, action, resourceEnum, resourceName, scopeEnum, scope))
                            .build();
                    nestedClassBuilder.addField(field);
                }
            }
            permissionsClassBuilder.addType(nestedClassBuilder.build());
        }

        // Tạo file Java
        JavaFile javaFile = JavaFile.builder(TARGET_PACKAGE, permissionsClassBuilder.build())
                .indent("    ") // 4 dấu cách
                .build();

        // Ghi file ra hệ thống file
        File sourceRoot = new File(JAVA_SOURCE_ROOT_PATH);
        javaFile.writeTo(sourceRoot.toPath());
    }

    private static void generatePermissionsTypeScript() throws IOException {
        StringBuilder tsContent = new StringBuilder();
        tsContent.append(buildTypeScriptFileDoc());
        tsContent.append("export const Permissions = {\n");

        // Lặp qua từng hằng số trong enum Resource (ví dụ: ENTERPRISE, USER, ...)
        for (Resource resource : Resource.values()) {
            String resourceName = resource.name();
            String nestedObjectName = toPascalCase(resourceName);

            tsContent.append("    /**\n")
                     .append("     * Các quyền liên quan đến tài nguyên: {@code ").append(resourceName).append("}\n")
                     .append("     */\n");
            tsContent.append("    ").append(nestedObjectName).append(": {\n");

            // Lấy danh sách Action và Scope hợp lệ và sắp xếp để đảm bảo output nhất quán
            List<Action> actions = new ArrayList<>(resource.getValidActions());
            actions.sort(Comparator.comparing(Enum::name));
            List<Scope> scopes = new ArrayList<>(resource.getValidScopes());
            scopes.sort(Comparator.comparing(Enum::name));

            // Tạo các hằng số quyền
            for (Action action : actions) {
                for (Scope scope : scopes) {
                    String fieldName = action.name() + "_" + scope.name();
                    // Sử dụng helper để đảm bảo logic tạo key nhất quán với backend
                    String permissionKey = PermissionHelper.buildPermissionKey(action, resource, scope);
                    tsContent.append("        ").append(fieldName).append(": '").append(permissionKey).append("',\n");
                }
            }
            tsContent.append("    },\n");
        }

        tsContent.append("} as const;\n\n");

        // Thêm một type helper để có thể sử dụng các chuỗi permission một cách type-safe
        tsContent.append("/** Union type của tất cả các chuỗi quyền có thể có trong hệ thống. */\n");
        tsContent.append("type PermissionObjects = (typeof Permissions)[keyof typeof Permissions];\n");
        tsContent.append("type AllValues<T> = T extends object ? T[keyof T] : never;\n");
        tsContent.append("export type PermissionString = AllValues<PermissionObjects>;\n");

        // Ghi file ra hệ thống file
        Path targetDirPath = Paths.get(TS_TARGET_DIR);
        Files.createDirectories(targetDirPath);
        Path outputFile = targetDirPath.resolve(TS_TARGET_FILENAME);

        try (FileWriter writer = new FileWriter(outputFile.toFile())) {
            writer.write(tsContent.toString());
        }
    }

    private static String buildTypeScriptFileDoc() {
        return "/**\n" +
               " * Chứa các hằng số cho các chuỗi quyền (permission strings) để sử dụng ở phía frontend.\n" +
               " *\n" +
               " * @see Permissions.java được sinh ra ở backend\n" +
               " *\n" +
               " * <b>LƯU Ý:</b> File này được sinh tự động bởi {@code PermissionClassGenerator.java}.\n" +
               " * Không chỉnh sửa file này trực tiếp. Thay vào đó, hãy cập nhật enum {@code vn.look.core.enums.Resource}\n" +
               " * và chạy lại phương thức main của generator để đồng bộ hóa.\n" +
               " */\n";
    }

    private static String toPascalCase(String s) {
        if (s == null || s.isEmpty()) {
            return "";
        }
        return Arrays.stream(s.split("_"))
                .map(word -> word.isEmpty() ? "" : Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                .collect(Collectors.joining());
    }

    private static CodeBlock buildClassJavadoc() {
        return CodeBlock.builder()
                .add("Lớp chứa các hằng số cho các chuỗi quyền (permission strings) trong toàn bộ hệ thống.\n")
                .add("<p><b>LƯU Ý:</b> File này được sinh tự động bởi {@code $L}.\n", "PermissionClassGenerator")
                .add("Không chỉnh sửa file này trực tiếp. Thay vào đó, hãy cập nhật enum {@link $T}\n", Resource.class)
                .add("và các annotation của nó, sau đó chạy lại phương thức main của generator.</p>\n")
                .add("và chạy lại phương thức main của generator.</p>\n")
                .build();
    }
}