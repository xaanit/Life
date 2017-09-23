package me.xaanit.life.internal.entities.executors;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import me.xaanit.life.internal.annotations.LifeExecutable;
import me.xaanit.life.internal.entities.LifeMethod;

public class TaskBuilder {

	private int maxWhileRepitions = -1;
	private int maxForRepitions = -1;
	private List<LifeMethod> methods = new ArrayList<>();
	private File file;
	private String[] args;

	/**
	 * Registers the methods in classes.
	 *
	 * @param classes The classes to register
	 * @return The current instance
	 */
	public TaskBuilder register(Class... classes) {
		for(Class clazz : classes) {
			for(Method m : clazz.getMethods()) {
				if(Modifier.isStatic(m.getModifiers()) && m.isAnnotationPresent(LifeExecutable.class)) {
					methods.add(new LifeMethod(clazz, m));
				}
			}
		}
		return this;
	}

	/**
	 * Unregisters all methods associated with the classes.
	 *
	 * @param classes The classes to unregister
	 * @return The current instance
	 */
	public TaskBuilder unregister(Class... classes) {
		for(Class clazz : classes) {
			methods.removeAll(
					methods.stream().filter(m -> m.getClazz() == clazz).collect(Collectors.toList()));
		}
		return this;
	}

	/**
	 * Sets the max amount of times a while loop can run in a task execution
	 *
	 * @param maxWhileRepitions The max amount of times a while loop can run
	 * @return The current instance
	 */
	public TaskBuilder withMaxWhileRepitions(int maxWhileRepitions) {
		this.maxWhileRepitions = maxWhileRepitions;
		return this;
	}

	/**
	 * Sets the max amount of times a for loop can run in a task execution
	 *
	 * @param maxForRepitions The max amount of times a for loop can run
	 * @return The current instance
	 */
	public TaskBuilder withMaxForRepitions(int maxForRepitions) {
		this.maxForRepitions = maxForRepitions;
		return this;
	}

	/**
	 * Sets the file to execute
	 *
	 * @param file The file to run
	 * @return The current instance
	 */
	public TaskBuilder withFile(File file) {
		this.file = file;
		return this;
	}

	/**
	 * Sets the file to execute
	 *
	 * @param path The path to the file
	 * @return The current instance
	 */
	public TaskBuilder withFile(String path) {
		this.file = new File(path);
		return this;
	}

	/**
	 * Sets the arguments for main
	 *
	 * @param args The arguments
	 * @return The current instance
	 */
	public TaskBuilder withArgs(String[] args) {
		this.args = args;
		return this;
	}

	/**
	 * Sets the arguments for main
	 *
	 * @param str The first argument
	 * @param args The rest of the arguments
	 * @return The current instance
	 */
	public TaskBuilder withArgs(String str,
			String... args) { // To avoid additional array creation sending a String[] to varargs
		this.args = new String[args.length + 1];
		this.args[0] = str;
		for(int i = 0; i < args.length; i++) {
			this.args[i + 1] = args[i];
		}
		return this;
	}

	public LifeTask build() {
		return new LifeTask(maxWhileRepitions, maxForRepitions, methods, file,
				Optional.ofNullable(args));
	}

	@Override
	public String toString() {
		return "TaskBuilder{" +
				"methods=" + methods +
				'}';
	}
}
