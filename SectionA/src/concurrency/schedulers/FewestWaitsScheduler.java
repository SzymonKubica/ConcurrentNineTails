package concurrency.schedulers;

import concurrency.ConcurrentProgram;
import concurrency.statements.WaitStmt;

import java.util.Comparator;
import java.util.stream.Collectors;

public class FewestWaitsScheduler extends RoundRobinScheduler implements Scheduler {
  @Override
  public int chooseAvailableThread(ConcurrentProgram program) {
    return program.getEnabledThreadIds().stream().sorted()
            .min(Comparator.comparingInt(id -> getNumberOfWaitStatements(id, program))).get();
  }

  private int getNumberOfWaitStatements(int threadId, ConcurrentProgram program) {
    return program.remainingStatements(threadId).stream()
            .filter( stmt -> stmt instanceof WaitStmt).collect(Collectors.toSet()).size();
  }
}
