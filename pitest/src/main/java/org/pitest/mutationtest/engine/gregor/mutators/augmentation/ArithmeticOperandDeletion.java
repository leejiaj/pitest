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

package org.pitest.mutationtest.engine.gregor.mutators.augmentation;

import java.util.HashMap;
import java.util.Map;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.pitest.mutationtest.engine.MutationIdentifier;
import org.pitest.mutationtest.engine.gregor.AbstractInsnMutator;
import org.pitest.mutationtest.engine.gregor.InsnSubstitution;
import org.pitest.mutationtest.engine.gregor.MethodInfo;
import org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import org.pitest.mutationtest.engine.gregor.MutationContext;
import org.pitest.mutationtest.engine.gregor.ZeroOperandMutation;

/*
 * To remove the second operand, remove the operator and pop (normal and long) second operand.
 * To remove the first operand, remove the operator, swap the two operands and remove the top one.
 * Because the second operand is above the first operand on the stack.
 */
public class ArithmeticOperandDeletion implements MethodMutatorFactory {

    public enum MutantType {

        REMOVE_FIRST_MUTATOR, REMOVE_LAST_MUTATOR;
    }

    private final MutantType mutatorType;

    public ArithmeticOperandDeletion(MutantType mt) {
        this.mutatorType = mt;
    }

    @Override
    public MethodVisitor create(final MutationContext context, final MethodInfo methodInfo,
            final MethodVisitor methodVisitor) {
        if (this.mutatorType == ArithmeticOperandDeletion.MutantType.REMOVE_FIRST_MUTATOR) {
            return new AODFirstMethodVisitor(this, methodInfo, context, methodVisitor);
        } else if (this.mutatorType == ArithmeticOperandDeletion.MutantType.REMOVE_LAST_MUTATOR) {
            return new AODLastMethodVisitor(this, context, methodVisitor);
        } else {
            return null;
        }
    }

    @Override
    public String getGloballyUniqueId() {
        return this.getClass().getName() + "_" + this.mutatorType.name();
    }

    @Override
    public String getName() {
        return "ArithmeticOperandDeletion - " + this.mutatorType.name();
    }
}

/*
 * Check the operator type, remove it with POP/POP2 and continue the replacement of the operands.
 */
class AODFirstMethodVisitor extends AbstractInsnMutator {

    AODFirstMethodVisitor(final MethodMutatorFactory factory, final MethodInfo methodInfo,
            final MutationContext context, final MethodVisitor writer) {
        super(factory, methodInfo, context, writer);
    }

    private static final Map<Integer, ZeroOperandMutation> MUTATIONS = new HashMap<Integer, ZeroOperandMutation>();

    static {
        MUTATIONS.put(Opcodes.IADD, new InsnSubstitution(Opcodes.POP,
                "REMOVE_SECOND_OPERATOR: Remove the second operand from an addition formula (int)"));
        MUTATIONS.put(Opcodes.DADD, new InsnSubstitution(Opcodes.POP2,
                "REMOVE_SECOND_OPERATOR: Remove the second operand from an addition formula (double)"));
        MUTATIONS.put(Opcodes.FADD, new InsnSubstitution(Opcodes.POP,
                "REMOVE_SECOND_OPERATOR: Remove the second operand from an addition formula (float)"));
        MUTATIONS.put(Opcodes.LADD, new InsnSubstitution(Opcodes.POP2,
                "REMOVE_SECOND_OPERATOR: Remove the second operand from an addition formula (long)"));

        MUTATIONS.put(Opcodes.ISUB, new InsnSubstitution(Opcodes.POP,
                "REMOVE_SECOND_OPERATOR: Remove the second operand from a subtraction formula (int)"));
        MUTATIONS.put(Opcodes.DSUB, new InsnSubstitution(Opcodes.POP2,
                "REMOVE_SECOND_OPERATOR: Remove the second operand from a subtraction formula (double)"));
        MUTATIONS.put(Opcodes.FSUB, new InsnSubstitution(Opcodes.POP,
                "REMOVE_SECOND_OPERATOR: Remove the second operand from a subtraction formula (float)"));
        MUTATIONS.put(Opcodes.LSUB, new InsnSubstitution(Opcodes.POP2,
                "REMOVE_SECOND_OPERATOR: Remove the second operand from a subtraction formula (long)"));

        MUTATIONS.put(Opcodes.IMUL, new InsnSubstitution(Opcodes.POP,
                "REMOVE_SECOND_OPERATOR: Remove the second operand from a multiplication formula (int)"));
        MUTATIONS.put(Opcodes.DMUL, new InsnSubstitution(Opcodes.POP2,
                "REMOVE_SECOND_OPERATOR: Remove the second operand from a multiplication formula (double)"));
        MUTATIONS.put(Opcodes.FMUL, new InsnSubstitution(Opcodes.POP,
                "REMOVE_SECOND_OPERATOR: Remove the second operand from a multiplication formula (float)"));
        MUTATIONS.put(Opcodes.LMUL, new InsnSubstitution(Opcodes.POP2,
                "REMOVE_SECOND_OPERATOR: Remove the second operand from a multiplication formula (long)"));

        MUTATIONS.put(Opcodes.IDIV, new InsnSubstitution(Opcodes.POP,
                "REMOVE_SECOND_OPERATOR: Remove the second operand from a division formula (int)"));
        MUTATIONS.put(Opcodes.DDIV, new InsnSubstitution(Opcodes.POP2,
                "REMOVE_SECOND_OPERATOR: Remove the second operand from a division formula (double)"));
        MUTATIONS.put(Opcodes.FDIV, new InsnSubstitution(Opcodes.POP,
                "REMOVE_SECOND_OPERATOR: Remove the second operand from a division formula (float)"));
        MUTATIONS.put(Opcodes.LDIV, new InsnSubstitution(Opcodes.POP2,
                "REMOVE_SECOND_OPERATOR: Remove the second operand from a division formula (long)"));

        MUTATIONS.put(Opcodes.IREM,
                new InsnSubstitution(Opcodes.POP, "REMOVE_SECOND_OPERATOR: Remove the second operand from a modulus formula (int)"));
        MUTATIONS.put(Opcodes.DREM, new InsnSubstitution(Opcodes.POP2,
                "REMOVE_SECOND_OPERATOR: Remove the second operand from a modulus formula (double)"));
        MUTATIONS.put(Opcodes.FREM, new InsnSubstitution(Opcodes.POP,
                "REMOVE_SECOND_OPERATOR: Remove the second operand from a modulus formula (float)"));
        MUTATIONS.put(Opcodes.LREM, new InsnSubstitution(Opcodes.POP2,
                "REMOVE_SECOND_OPERATOR: Remove the second operand from a modulus formula (long)"));
    }

    @Override
    protected Map<Integer, ZeroOperandMutation> getMutations() {
        return MUTATIONS;
    }

}

class AODLastMethodVisitor extends MethodVisitor {

    private final MethodMutatorFactory factory;
    private final MutationContext context;

    AODLastMethodVisitor(final MethodMutatorFactory factory, final MutationContext context,
            final MethodVisitor methodVisitor) {
        super(Opcodes.ASM6, methodVisitor);
        this.factory = factory;
        this.context = context;

    }

    @Override
    public void visitInsn(int opcode) {
        if ((opcode == Opcodes.IADD) || (opcode == Opcodes.FADD)) {
            replaceSmallAddOperand(opcode);
        } else if ((opcode == Opcodes.ISUB) || (opcode == Opcodes.FSUB)) {
            replaceSmallSubOperand(opcode);
        } else if ((opcode == Opcodes.IMUL) || (opcode == Opcodes.FMUL)) {
            replaceSmallMulOperand(opcode);
        } else if ((opcode == Opcodes.IDIV) || (opcode == Opcodes.FDIV)) {
            replaceSmallDivOperand(opcode);
        } else if ((opcode == Opcodes.IREM) || (opcode == Opcodes.DREM) || (opcode == Opcodes.FREM)) {
            replaceSmallRemOperand(opcode);
        } else if ((opcode == Opcodes.LADD) || (opcode == Opcodes.LSUB) || (opcode == Opcodes.LMUL)
                || (opcode == Opcodes.LDIV) || (opcode == Opcodes.LREM)) {
            replaceLongOperand(opcode);
        } else if ((opcode == Opcodes.DADD) || (opcode == Opcodes.DSUB) || (opcode == Opcodes.DMUL)
                || (opcode == Opcodes.DDIV) || (opcode == Opcodes.DREM)) {
            replaceDoubleOperand(opcode);
        } else {
            super.visitInsn(opcode);
        }

    }

    private void replaceSmallAddOperand(int opcode) {
        final MutationIdentifier muID = this.context.registerMutation(factory,
                "REMOVE_FIRST_OPERAND: Remove the first operand from an addition formula");

        if (this.context.shouldMutate(muID)) {
            removeSmallFirstOperand();
        } else {
            super.visitInsn(opcode);
        }
    }

    private void replaceSmallSubOperand(int opcode) {
        final MutationIdentifier muID = this.context.registerMutation(factory,
                "REMOVE_FIRST_OPERAND: Remove the first operand from a subtraction formula");

        if (this.context.shouldMutate(muID)) {
            removeSmallFirstOperand();
        } else {
            super.visitInsn(opcode);
        }
    }

    private void replaceSmallMulOperand(int opcode) {
        final MutationIdentifier muID = this.context.registerMutation(factory,
                "REMOVE_FIRST_OPERAND: Remove the first operand from a multiplication formula");

        if (this.context.shouldMutate(muID)) {
            removeSmallFirstOperand();
        } else {
            super.visitInsn(opcode);
        }
    }

    private void replaceSmallDivOperand(int opcode) {
        final MutationIdentifier muID = this.context.registerMutation(factory,
                "REMOVE_FIRST_OPERAND: Remove the first operand from a division formula");

        if (this.context.shouldMutate(muID)) {
            removeSmallFirstOperand();
        } else {
            super.visitInsn(opcode);
        }
    }

    private void replaceSmallRemOperand(int opcode) {
        final MutationIdentifier muID = this.context.registerMutation(factory,
                "REMOVE_FIRST_OPERAND: Remove the first operand from a modulus formula");

        if (this.context.shouldMutate(muID)) {
            removeSmallFirstOperand();
        } else {
            super.visitInsn(opcode);
        }
    }

    private void replaceLongOperand(int opcode) {
        final MutationIdentifier muID = this.context.registerMutation(factory,
                "REMOVE_FIRST_OPERAND: Remove the first operand of from a formula involving longs");

        if (this.context.shouldMutate(muID)) {
            removeLargeFirstOperand();
        } else {
            super.visitInsn(opcode);
        }
    }

    private void replaceDoubleOperand(int opcode) {
        final MutationIdentifier muID = this.context.registerMutation(factory,
                "REMOVE_FIRST_OPERAND: Remove the first operand of a formula involving doubles");

        if (this.context.shouldMutate(muID)) {
            removeLargeFirstOperand();
        } else {
            super.visitInsn(opcode);
        }
    }

    /*
     * Swap the second and first operand, then remove the top operand. This is short
     * hand for removing the first operand. This works for
     */
    private void removeSmallFirstOperand() {
        super.visitInsn(Opcodes.SWAP);
        super.visitInsn(Opcodes.POP);
    }

    /*
     * DUP2_X2 duplicates the second operand and put it below the first operand on
     * the stack. Then, pop the second operand and the first operand. This is the
     * same as using SWAP long values and POP2.
     */
    private void removeLargeFirstOperand() {
        super.visitInsn(Opcodes.DUP2_X2);
        super.visitInsn(Opcodes.POP2);
        super.visitInsn(Opcodes.POP2);
    }
}
