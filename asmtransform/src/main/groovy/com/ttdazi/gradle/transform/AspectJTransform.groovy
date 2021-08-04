package com.ttdazi.gradle.transform

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import groovyjarjarasm.asm.tree.AnnotationNode
import groovyjarjarasm.asm.tree.ClassNode
import jdk.internal.org.objectweb.asm.ClassReader
import org.apache.commons.io.FileUtils

class AspectJTransform extends Transform {
    @Override
    String getName() {
        return "AspectJTransform";
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    boolean isIncremental() {
        return false;
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {

        boolean isIncremental = transformInvocation.isIncremental()

        //OutputProvider管理输出路径，如果消费型输入为空，你会发现OutputProvider == null
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider()

        if (!isIncremental) {
            //不需要增量编译，先清除全部
            outputProvider.deleteAll();
        }
        transformInvocation.getInputs().each { TransformInput input ->
//            input.jarInputs.each { JarInput jarInput ->
//                //处理Jar
//                processJarInputWithIncremental(jarInput, outputProvider, isIncremental)
//            }
            input.directoryInputs.each { DirectoryInput directoryInput ->
                //处理文件
                processDirectoryInputWithIncremental(directoryInput, outputProvider, isIncremental)
            }
        }
    }

    void processDirectoryInputWithIncremental(DirectoryInput directoryInput, TransformOutputProvider outputProvider, boolean isIncremental) {
        File dest = outputProvider.getContentLocation(
                directoryInput.getFile().getAbsolutePath(),
                directoryInput.getContentTypes(),
                directoryInput.getScopes(),
                Format.DIRECTORY)
        if (isIncremental) {
            //处理增量编译
            processDirectoryInputWhenIncremental(directoryInput, dest)
        } else {
            processDirectoryInput(directoryInput, dest)
        }
    }

    void processDirectoryInputWhenIncremental(DirectoryInput directoryInput, File dest) {
        FileUtils.forceMkdir(dest)
        String srcDirPath = directoryInput.getFile().getAbsolutePath()
        String destDirPath = dest.getAbsolutePath()
        Map<File, Status> fileStatusMap = directoryInput.getChangedFiles()
        fileStatusMap.each { Map.Entry<File, Status> entry ->
            File inputFile = entry.getKey()
            Status status = entry.getValue()
            String destFilePath = inputFile.getAbsolutePath().replace(srcDirPath, destDirPath)
            File destFile = new File(destFilePath)
            switch (status) {
                case Status.NOTCHANGED:
                    break
                case Status.REMOVED:
                    if (destFile.exists()) {
                        FileUtils.forceDelete(destFile)
                    }
                    break
                case Status.ADDED:
                case Status.CHANGED:
                    FileUtils.touch(destFile)
                    transformSingleFile(inputFile, destFile, srcDirPath)
                    break
            }
        }
    }

    void processDirectoryInput(DirectoryInput directoryInput, File dest) {
        transformDirectoryInput(directoryInput.getFile(), dest)
    }

    void transformDirectoryInput(File input, File dest) {
        //TODO do some transform
        //将修改过的字节码copy到dest，就可以实现编译期间干预字节码的目的了
//        ASM生成字节码
        if (dest.exists()) {
            FileUtils.deleteDirectory(dest)
        }
        String srcDirPath = input.getAbsolutePath()
        String destDirPath = dest.getAbsolutePath()
        println("=== transform dir = " + srcDirPath + ", " + destDirPath)

        for (File file : input.listFiles()) {
            String destFilePath = file.absolutePath.replace(srcDirPath, destDirPath)
            File destFile = new File(destFilePath)
            if (file.isDirectory()) {
                transformDirectoryInput(file, destFile)
            } else if (file.isFile()) {
                FileUtils.touch(destFile)
                transformSingleFile(file, destFile)
            }
        }

        FileUtils.copyDirectory(input, dest)
    }

    void transformSingleFile(File inputFile, File destFile, String srcDirPath) {
        def is = new FileInputStream(inputFile)
        ClassReader reader = new ClassReader(is)
        ClassNode node = new ClassNode()
        reader.accept(node, 1)
        def list = node.invisibleAnnotations
        for (AnnotationNode an : list) {
            if (ANNOTATION_DESC == an.desc) {
                def path = an.values[1]
                routeMap[path] = className
                break
            }
        }
        FileUtils.copyFile(inputFile, destFile)
    }
}
