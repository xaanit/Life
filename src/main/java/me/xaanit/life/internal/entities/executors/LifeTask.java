package me.xaanit.life.internal.entities.executors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import me.xaanit.life.internal.entities.LifeMethod;
import me.xaanit.life.internal.entities.UserMethod;
import me.xaanit.life.internal.entities.token.Token;
import me.xaanit.life.internal.entities.token.Tokeniser;
import me.xaanit.life.internal.exceptions.LifeException;

/**
 * Represents a task to execute. Thanks to {@url https://github.com/arraying/zeus} (friend) for the
 * idea.
 */
public final class LifeTask {

	private final int maxWhileRepitions;
	private final int maxForRepitions;
	private final List<LifeMethod> methods;
	private static final Logger logger = Logger.getLogger("Life");
	private final AtomicInteger currentWhileRepitions;
	private final AtomicInteger currentForRepitions;
	private final File file;
	private final String[] args;
	private final Tokeniser tokeniser;

	protected LifeTask(final int maxWhileRepitions, final int maxForRepitions,
			final List<LifeMethod> methods, final File file, final Optional<String[]> args) {
		if(file == null) {
			throw new LifeException("File can not be null.");
		}

		this.maxWhileRepitions = maxWhileRepitions;
		this.maxForRepitions = maxForRepitions;
		this.methods = methods;
		this.currentWhileRepitions = maxWhileRepitions < 0 ? null : new AtomicInteger();
		this.currentForRepitions = maxForRepitions < 0 ? null : new AtomicInteger();
		this.args = args.isPresent() ? args.get() : new String[0];
		this.file = file;
		tokeniser = new Tokeniser(this);
	}

	/**
	 * Executes the task,
	 *
	 * @return True if there was no errors
	 * @throws LifeException If there's a problem on execution
	 */
	public boolean execute() throws LifeException {
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			String toTokenise = "";
			while((line = br.readLine()) != null) {
				toTokenise += line + "\n";
			}

			List<Token> methods = tokeniser.topLevel(toTokenise);

			methods.stream().filter(t ->
					t.getInfo() instanceof UserMethod
			).forEach(System.out::println);
			return true;
		} catch(IOException ex) {
			throw new LifeException("Could not read file. " + ex.getMessage());
		}
	}


}
