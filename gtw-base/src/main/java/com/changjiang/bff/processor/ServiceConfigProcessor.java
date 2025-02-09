package com.changjiang.bff.processor;

import com.changjiang.bff.annotation.ServiceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

/**
 * ServiceConfigProcessor 是一个注解处理器，用于处理带有 @ServiceConfig 注解的类。
 * 它在编译时扫描项目中所有使用了 @ServiceConfig 注解的元素，并将这些元素的相关信息写入到一个文件中。
 * SupportedSourceVersion
 *  指定该注解处理器支持的 Java 源代码版本为 Java 17。
 *  * 这意味着该处理器只能处理使用 Java 17 或更低版本语法编写的源代码。
 *  * 如果项目中使用了高于 Java 17 的语法特性，该处理器将无法正确处理这些代码。
 */
@SupportedAnnotationTypes("com.changjiang.bff.annotation.ServiceConfig")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class ServiceConfigProcessor extends AbstractProcessor {

    // 日志记录器，用于记录处理过程中的日志信息
    private static final Logger logger = LoggerFactory.getLogger(ServiceConfigProcessor.class);

    /**
     * 初始化方法，接收编译环境。
     *
     * @param processingEnv 编译环境
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    /**
     * 处理带有 @ServiceConfig 注解的元素。
     * 遍历所有被 @ServiceConfig 注解标记的元素，提取它们的包名和简单名称，并将这些信息写入到 serviceconfig.classes 文件中。
     *
     * @param annotations 当前轮次中处理的注解集合
     * @param roundEnv 当前轮次的环境信息
     * @return 返回 true 表示已处理完相关注解，返回 false 表示未处理任何注解
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try (Writer writer = processingEnv.getFiler().createResource(
                javax.tools.StandardLocation.CLASS_OUTPUT, "", "serviceconfig.classes").openWriter()) {
            // 遍历所有被 @ServiceConfig 注解标记的元素
            for (Element element : roundEnv.getElementsAnnotatedWith(ServiceConfig.class)) {
                if (element.getEnclosingElement() instanceof TypeElement) {
                    TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
                    // 写入包名和简单名称到文件中
                    writer.write(enclosingElement.getQualifiedName() + "#" + element.getSimpleName() + "\n");
                }
            }
        } catch (IOException e) {
            // 记录写入文件时发生的异常
            logger.error("Failed to write serviceconfig.classes file", e);
        }
        return true;
    }
}
