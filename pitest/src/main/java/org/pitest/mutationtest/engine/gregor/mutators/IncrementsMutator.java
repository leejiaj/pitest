/*
 * Copyright 2010 Henry Coles
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and limitations under the License. 
 */
package org.pitest.mutationtest.engine.gregor.mutators;

import org.objectweb.asm.MethodVisitor;
import org.pitest.mutationtest.engine.MutationIdentifier;
import org.pitest.mutationtest.engine.gregor.Context;
import org.pitest.mutationtest.engine.gregor.LineTrackingMethodAdapter;
import org.pitest.mutationtest.engine.gregor.MethodInfo;
import org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;

public enum IncrementsMutator implements MethodMutatorFactory {

  INCREMENTS_MUTATOR;

  public MethodVisitor create(final Context context,
      final MethodInfo methodInfo, final MethodVisitor methodVisitor) {
    return new IncrementsMethodVisitor(methodInfo, context, methodVisitor);
  }
}

class IncrementsMethodVisitor extends LineTrackingMethodAdapter {

  public IncrementsMethodVisitor(final MethodInfo methodInfo,
      final Context context, final MethodVisitor writer) {
    super(methodInfo, context, writer);
  }

  @Override
  public void visitIincInsn(final int var, final int increment) {
    final MutationIdentifier newId = this.context.registerMutation(
        IncrementsMutator.class, "Changed increment from " + increment + " to "
            + -increment);
    if (this.context.shouldMutate(newId)) {
      this.mv.visitIincInsn(var, -increment);
    } else {
      this.mv.visitIincInsn(var, increment);
    }
  }

}