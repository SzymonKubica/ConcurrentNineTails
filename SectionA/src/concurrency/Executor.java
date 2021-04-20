package concurrency;

import java.util.LinkedList;
import java.util.List;

import concurrency.schedulers.Scheduler;

public class Executor {

  private ConcurrentProgram program;
  private Scheduler scheduler;

  public Executor(ConcurrentProgram program, Scheduler scheduler) {
    this.program = program;
    this.scheduler = scheduler;
  }

  /**
   * Executes program with respect to scheduler
   *
   * @return the final state and history of execution
   */
  public String execute() {
    List<Integer> history = new LinkedList<>();
    boolean deadlockOccurred = false;

    while (!program.isTerminated() && !deadlockOccurred) {
      try {
        int nextThreadId = scheduler.chooseThread(program);
        history.add(nextThreadId);
        program.step(nextThreadId);
      } catch (DeadlockException e) {
        deadlockOccurred = true;
      }
    }

    StringBuilder result = new StringBuilder();
    result.append("Final state: " + program + "\n");
    result.append("History: " + history + "\n");
    result.append("Termination status: "
            + (deadlockOccurred ? "deadlock" : "graceful") + "\n");
    return result.toString();
  }

  // An incorrect attempt at overriding the equals method
  // of Object
  @Override
  public boolean equals(Object that) {
    if (that instanceof Executor) {
      return this.program.toString().equals(((Executor) that).program.toString());
    } else {
      return false;
    }
  }

  // While overriding equals we also need to override hashCode
  @Override
  public int hashCode() {
    return this.program.toString().hashCode();
  }

}
