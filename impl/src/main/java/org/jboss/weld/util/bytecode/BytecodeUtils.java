/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.weld.util.bytecode;

import org.jboss.classfilewriter.code.CodeAttribute;

/**
 * utility class for common bytecode operations
 *
 * @author Stuart Douglas
 */
public class BytecodeUtils {

    private BytecodeUtils() {
    }

    /**
     * Adds the correct load instruction based on the type descriptor
     *
     * @param code     the bytecode to add the instruction to
     * @param type     the type of the variable
     * @param variable the variable number
     */
    public static void addLoadInstruction(CodeAttribute code, String type, int variable) {
        char tp = type.charAt(0);
        if (tp != 'L' && tp != '[') {
            // we have a primitive type
            switch (tp) {
                case 'J':
                    code.lload(variable);
                    break;
                case 'D':
                    code.dload(variable);
                    break;
                case 'F':
                    code.fload(variable);
                    break;
                default:
                    code.iload(variable);
            }
        } else {
            code.aload(variable);
        }
    }

    /**
     * Pushes a class type onto the stack from the string representation This can
     * also handle primitives
     *
     * @param b         the bytecode
     * @param classType the type descriptor for the class or primitive to push.
     *                  This will accept both the java.lang.Object form and the
     *                  Ljava/lang/Object; form
     */
    public static void pushClassType(CodeAttribute b, String classType) {
        if (classType.length() != 1) {
            if (classType.startsWith("L") && classType.endsWith(";")) {
                classType = classType.substring(1, classType.length() - 1);
            }
            b.loadClass(classType);
        } else {
            char type = classType.charAt(0);
            switch (type) {
                case 'I':
                    b.getstatic(Integer.class.getName(), "TYPE", "Ljava/lang/Class;");
                    break;
                case 'J':
                    b.getstatic(Long.class.getName(), "TYPE", "Ljava/lang/Class;");
                    break;
                case 'S':
                    b.getstatic(Short.class.getName(), "TYPE", "Ljava/lang/Class;");
                    break;
                case 'F':
                    b.getstatic(Float.class.getName(), "TYPE", "Ljava/lang/Class;");
                    break;
                case 'D':
                    b.getstatic(Double.class.getName(), "TYPE", "Ljava/lang/Class;");
                    break;
                case 'B':
                    b.getstatic(Byte.class.getName(), "TYPE", "Ljava/lang/Class;");
                    break;
                case 'C':
                    b.getstatic(Character.class.getName(), "TYPE", "Ljava/lang/Class;");
                    break;
                case 'Z':
                    b.getstatic(Boolean.class.getName(), "TYPE", "Ljava/lang/Class;");
                    break;
            }
        }
    }
}
