/*
*  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*/
package org.wso2.ballerina.core.utils;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.ballerinalang.BLangProgramLoader;
import org.wso2.ballerina.core.interpreter.SymScope;
import org.wso2.ballerina.core.model.BLangPackage;
import org.wso2.ballerina.core.model.BLangProgram;
import org.wso2.ballerina.core.model.BallerinaFile;
import org.wso2.ballerina.core.model.GlobalScope;
import org.wso2.ballerina.core.model.builder.BLangModelBuilder;
import org.wso2.ballerina.core.model.types.BTypes;
import org.wso2.ballerina.core.parser.BallerinaLexer;
import org.wso2.ballerina.core.parser.BallerinaParser;
import org.wso2.ballerina.core.parser.BallerinaParserErrorStrategy;
import org.wso2.ballerina.core.parser.antlr4.BLangAntlr4Listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility methods for Ballerina Parser.
 *
 * @since 0.8.0
 */
public class BTestUtils {

    private BTestUtils() {
    }

    /**
     * Get parsed, analyzed and linked Ballerina object model.
     *
     * @param sourceFilePath Path to Bal file.
     * @return BallerinaFile instance.
     */
    public static BLangProgram parseBalFile(String sourceFilePath) {

        Path programPath = Paths.get(BTestUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        return new BLangProgramLoader().loadLibrary(programPath,
                Paths.get(sourceFilePath));
    }

    /**
     * Get parsed, analyzed and linked Ballerina object model.
     *
     * @param sourceFilePath Path to Bal file.
     * @param globalSymScope Global symbol scope which includes all the native functions and actions
     * @return BallerinaFile instance.
     */
/*    public static BallerinaFile parseBalFile(String sourceFilePath, SymScope globalSymScope) {

        BallerinaParser ballerinaParser = getBallerinaParser(sourceFilePath);

        // Create Ballerina model builder class
        GlobalScope globalScope = GlobalScope.getInstance();
        BTypes.loadBuiltInTypes(globalScope);
        BLangProgram bLangProgram = new BLangProgram(globalScope, BLangProgram.Category.MAIN_PROGRAM);
//        BLangPackage.PackageBuilder packageBuilder = new BLangPackage.PackageBuilder()
        BLangPackage bLangPackage = new BLangPackage(bLangProgram);
        BLangModelBuilder modelBuilder = null;

//        new BLangModelBuilder(bLangPackage,
//                Paths.get(sourceFilePath).getFileName().toString());


        BLangAntlr4Listener langModelBuilder = new BLangAntlr4Listener(modelBuilder);

        ballerinaParser.addParseListener(langModelBuilder);
        ballerinaParser.setErrorHandler(new BallerinaParserErrorStrategy());
        ballerinaParser.compilationUnit();

        // Get the model for source file
        BallerinaFile bFile = modelBuilder.build();

        // Analyze semantic properties of the source code
//        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(bFile, bLangPackage);
//        bFile.accept(semanticAnalyzer);

        return bFile;
    }*/

    /**
     * Get Ballerina Parser instance for given Ballerina file.
     *
     * @param path as a String.
     * @return BallerinaParser instance.
     */
    public static BallerinaParser getBallerinaParser(String path) {
        URL fileResource = BTestUtils.class.getClassLoader().getResource(path);
        if (fileResource == null) {
            throw new RuntimeException("Source file is not available: " + path);
        }

        ANTLRInputStream antlrInputStream = null;
        try {
            File file = new File(fileResource.getFile());
            antlrInputStream = new ANTLRInputStream(new FileInputStream(file));
            antlrInputStream.name = file.getName();
        } catch (IOException e) {
            throw new RuntimeException("Unable read file: " + path, e);
        }

        BallerinaLexer ballerinaLexer = new BallerinaLexer(antlrInputStream);
        CommonTokenStream ballerinaToken = new CommonTokenStream(ballerinaLexer);
        return new BallerinaParser(ballerinaToken);
    }

}
