package cn.bsd.learn.butterknife.complier;

import com.google.auto.service.AutoService;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

import cn.bsd.learn.butterknife.annotation.BindView;
import cn.bsd.learn.butterknife.annotation.OnClick;

@AutoService(Processor.class)//这里才是触发注解处理器，生成java文件的g点
//@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ButterKnifeProcess extends AbstractProcessor {

    private Elements elementUtils;//Elements中包含用于操作Element的工具方法
    private Filer filer;//Filter用来创建新的源文件，class文件以及辅助文件

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        filer = processingEnvironment.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        //添加支持BindView和OnClick注解的类型
        Set<String> types = new LinkedHashSet<>();
        types.add(BindView.class.getCanonicalName());
        types.add(OnClick.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        //返回此注释Processor支持的最新的源版本，该方法可以通过注解@SupportedSourceVersion指定
        return SourceVersion.latest();
    }

    //注解处理器的核心方法，处理具体的注解，生成java文件
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        System.out.println("开始..................................");
        //获取MainActivity中所有带BindView注解的属性
        Set<? extends Element> bindViewSet = roundEnvironment.getElementsAnnotatedWith(BindView.class);
        //保存键值对,key:"cn.bsd.learn.butterknife.sample.MainActivity"
        //value:MainActivity中所有带BindView注解的属性
        Map<String, List<VariableElement>> bindViewMap = new HashMap<>();
        //遍历
        for (Element element : bindViewSet) {
            //转成原始属性元素（结构体元素）
            VariableElement variableElement = (VariableElement) element;
            //通过属性元素获取它所属的MainActivity类名，如cn.bsd.learn.butterknife.sample.MainActivity
            String activityName = getActivityName(variableElement);
            //从map里面找
            List<VariableElement> list = bindViewMap.get(activityName);
            if (list == null) {
                list = new ArrayList<>();
                bindViewMap.put(activityName,list);
            }
            list.add(variableElement);
        }


        //-----------------------OnClick---------------------------
        //获取MainActivity中所有带OnClick注解的属性
        Set<? extends Element> onClickSet = roundEnvironment.getElementsAnnotatedWith(OnClick.class);
        //保存键值对,key:"cn.bsd.learn.butterknife.sample.MainActivity"
        //value:MainActivity中所有带BindView注解的属性
        Map<String, List<ExecutableElement>> onClickMap = new HashMap<>();
        //遍历
        for (Element element : onClickSet) {
            //转成原始属性元素（结构体元素）
            ExecutableElement executableElement = (ExecutableElement) element;
            //通过属性元素获取它所属的MainActivity类名，如cn.bsd.learn.butterknife.sample.MainActivity
            String activityName = getActivityName(executableElement);
            //从map里面找
            List<ExecutableElement> list = onClickMap.get(activityName);
            if (list == null) {
                list = new ArrayList<>();
                onClickMap.put(activityName,list);
            }
            list.add(executableElement);
        }

        //-----------------------造币器---------------------------
        //造币器，一行一行代码写的时候，有上面的集合存储
        for(String activityName : bindViewMap.keySet()){//
            //获取"cn.bsd.learn.butterknife.sample.MainActivity"所有的控件和方法的集合
            List<VariableElement> cacheElement = bindViewMap.get(activityName);
            List<ExecutableElement> clickElement = onClickMap.get(activityName);

            try {
                //创建一个新的源文件（class），并返回一个对象可以写入
                JavaFileObject javaFileObject = filer.createSourceFile(activityName + "$ViewBinder");
                String packageName = getPackageName(cacheElement.get(0));
                Writer writer = javaFileObject.openWriter();

                String activitySimpleName = cacheElement.get(0).getEnclosingElement()
                        .getSimpleName().toString()+"$ViewBinder";

                //第一行生成包
                writer.write("package "+packageName+";\n");
                //第二行生成要导入的接口类（必须手动导入）
                writer.write("import cn.bsd.learn.butterknife.library.ViewBinder;\n");
                writer.write("import cn.bsd.learn.butterknife.library.DebouncingOnClickListener;\n");
                writer.write("import android.view.View;\n");
                writer.write("import android.util.Log;\n");

                //第三行生成类MainActivity$ViewBinder
                writer.write("public class "+ activitySimpleName
                        +" implements ViewBinder<" + activityName+"> {\n");

                //第四行生成bind方法
                writer.write("public void bind(final "+activityName+" target) {\n");

                //循环生成MainActivity每个控件属性
                for (VariableElement variableElement : cacheElement) {
                    //控件属性名
                    String fieldName = variableElement.getSimpleName().toString();
                    //获取控件的注解
                    BindView bindView = variableElement.getAnnotation(BindView.class);
                    //获取控件注解的id值
                    int id = bindView.value();
                    //生成：target.tv = target.findViewById(xxx);
                    writer.write("target." + fieldName +" = " + "target.findViewById("+id+");\n");
                }

                writer.write("for (int i= 0;i<100;i++) {\n");
                writer.write("Log.e(\"Learn >>>\",\"learn\" + i);\n}\n");

                //循环生成MainActivity每个点击事件
                for (ExecutableElement executableElement : clickElement) {
                    //获取方法名
                    String methodName = executableElement.getSimpleName().toString();
                    //获取方法的注解
                    OnClick onClick = executableElement.getAnnotation(OnClick.class);
                    //获取方法注解的id值
                    int id = onClick.value();
                    //获取方法参数
                    List<? extends VariableElement> parameters = executableElement.getParameters();

                    //生成点击事件
                    writer.write("target.findViewById("+id+").setOnClickListener(new DebouncingOnClickListener(){\n");
                    writer.write("public void doClick(View view) {\n");
                    if(parameters.isEmpty()){
                        writer.write("target."+methodName+"();\n}\n});\n");
                    }else {
                        writer.write("target."+methodName+"(view);\n}\n});\n");
                    }
                }

                writer.write("\nlearn();\n}\n");
                writer.write("public void learn() {Log.e(\"Learn >>>\",\"hello learn\");\n");

                writer.write("\n}\n}");
                System.out.println("结束..................................");
                writer.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }


        return false;
    }

    /**
     * 通过属性标签获取类名标签，再通过类名标签获取包名标签
     * @param variableElement 属性标签
     * @return cn.bsd.learn.butterknife.sample.MainActivity （包名+类名）
     */
    private String getActivityName(VariableElement variableElement) {
        //通过属性标签获取类名标签，再通过类名标签获取包名标签
        String packageName = getPackageName(variableElement);
        //通过属性标签获取类名标签
        TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
        //完整字符串拼接：cn.bsd.learn.butterknife.sample+"."+MainActivity
        return packageName+"."+typeElement.getSimpleName().toString();
    }

    //通过属性标签获取类名标签，再通过类名标签获取包名标签（通过属性节点，找到父节点，再找到父节点的父节点）
    private String getPackageName(VariableElement variableElement) {
        //通过属性标签获取类名标签
        TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
        //通过类名标签获取包名标签
        String packageName = elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
        System.out.println("packageName >>> "+packageName);
        return packageName;
    }

    private String getActivityName(ExecutableElement executableElement) {
        //通过方法标签获取类名标签，再通过类名标签获取包名标签
        String packageName = getPackageName(executableElement);
        //通过方法标签获取类名标签
        TypeElement typeElement = (TypeElement) executableElement.getEnclosingElement();
        //完整字符串拼接：cn.bsd.learn.butterknife.sample+"."+MainActivity
        return packageName+"."+typeElement.getSimpleName().toString();
    }

    private String getPackageName(ExecutableElement executableElement) {
        //通过方法标签获取类名标签
        TypeElement typeElement = (TypeElement) executableElement.getEnclosingElement();
        //通过类名标签获取包名标签
        String packageName = elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
        System.out.println("packageName >>> "+packageName);
        return packageName;
    }


}
