package ru.uskov.apidiff.classesmodel;

import org.objectweb.asm.tree.*;
import ru.uskov.apidiff.classesmodel.objectnaming.ObjectNameMapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static org.objectweb.asm.tree.AbstractInsnNode.*;


/**
 * Class that reads instruction list for method
 */
public class InstructionInstanceFactory {

    private final ObjectNameMapper mapper;

    public InstructionInstanceFactory(ObjectNameMapper objectNameMapper) {
        this.mapper = objectNameMapper;
    }

    private String convertName(String rawType) {
        return mapper.convertClassName(rawType);
    }


    /**
     * The method that parses instructions list into internal representation.  NOP and breakpoint operands are skipped.
     * @param instructions instruction list in the ASM format
     * @return list of {@link InstructionInstance}
     */
    public List<InstructionInstance> getInstructions(InsnList instructions) {
        Iterator<AbstractInsnNode> it = instructions.iterator();
        List<InstructionInstance> result = new ArrayList<>();
        if (! it.hasNext()) {
            return result;
        }
        for (AbstractInsnNode insnNode = it.next(); it.hasNext(); insnNode = it.next()) {
            ImmutableInstructionInstance.Builder builder = ImmutableInstructionInstance.builder().opcode(insnNode.getOpcode());
            // final modifier ensures us that we don't forget add break/set variable
            final InstructionInstance instance;
            switch (insnNode.getType()) {
                // These instructions do not affect behavior
                case INSN:
                case LABEL:
                case FRAME:
                case LINE:
                    continue;

                case INT_INSN:
                    instance = builder.addParameters(((IntInsnNode)insnNode).operand).build();
                    break;
                case VAR_INSN:
                    instance = builder.addParameters(((VarInsnNode)insnNode).var).build();
                    break;
                case TYPE_INSN:
                    instance = builder.addParameters(convertName(((TypeInsnNode)insnNode).desc)).build();
                    break;

                // Reference to class
                case FIELD_INSN:
                    //TODO actually desc should be parsed here in order to handle classes renames
                    FieldInsnNode fieldInsnNode = (FieldInsnNode)insnNode;
                    instance = builder.addParameters(convertName(fieldInsnNode.owner), fieldInsnNode.name, fieldInsnNode.desc).build();
                    break;
                case METHOD_INSN:
                    //TODO actually desc should be parsed here in order to handle classes renames
                    MethodInsnNode methodInsnNode = (MethodInsnNode)insnNode;
                    instance = builder.addParameters(convertName(methodInsnNode.owner), methodInsnNode.name, methodInsnNode.desc).build();
                    break;
                case INVOKE_DYNAMIC_INSN:
                    //TODO actually desc should be parsed here in order to handle classes renames
                    InvokeDynamicInsnNode invokeDynamicInsnNode = (InvokeDynamicInsnNode)insnNode;
                    instance = builder.addParameters(convertName(invokeDynamicInsnNode.bsm.getOwner()), invokeDynamicInsnNode.name, invokeDynamicInsnNode.desc).build();
                    break;

                // offcet
                case JUMP_INSN:
                    // TODO properly resolve offsets and add to parameters
                    instance = builder.build();
                    break;

                case LDC_INSN:
                    instance = builder.addParameters(((LdcInsnNode)insnNode).cst).build();
                    break;

                case IINC_INSN:
                    IincInsnNode iincInsnNode= (IincInsnNode)insnNode;
                    instance = builder.addParameters(iincInsnNode.var, iincInsnNode.incr).build();
                    break;

                case TABLESWITCH_INSN:
                    // TODO properly resolve offsets and add to parameters
                    TableSwitchInsnNode tableSwitchInsnNode = (TableSwitchInsnNode)insnNode;
                    instance = builder.addParameters(tableSwitchInsnNode.min, tableSwitchInsnNode.max).build();
                    break;

                case LOOKUPSWITCH_INSN:
                    // TODO properly resolve offsets and add to parameters
                    instance = builder.build();
                    break;

                case MULTIANEWARRAY_INSN:
                    //TODO actually desc should be parsed here in order to handle classes renames
                    MultiANewArrayInsnNode multiANewArrayInsnNode = (MultiANewArrayInsnNode) insnNode;
                    instance = builder.addParameters(multiANewArrayInsnNode.desc, multiANewArrayInsnNode.dims).build();
                    break;

                default: throw new IllegalStateException("Unknown opcode type " + insnNode.getType());
            }
            result.add(instance);
        }
        return result;
    }
}
