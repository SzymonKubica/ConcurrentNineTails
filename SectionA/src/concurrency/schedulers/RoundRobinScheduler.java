package concurrency.schedulers;

import concurrency.ConcurrentProgram;
import concurrency.DeadlockException;

import java.util.stream.Collectors;

public class RoundRobinScheduler implements Scheduler {
  private int invocationCount = 0;
  private int lastChosenId;

  @Override
  public int chooseThread(ConcurrentProgram program) throws DeadlockException {
    if (program.getEnabledThreadIds().size() == 0) {
      throw new DeadlockException();
    } else {
      return chooseAvailableThread(program);
    }
  }

  protected int chooseAvailableThread(ConcurrentProgram program) {
    int currentId;
    if (invocationCount == 0) {
      currentId = getMinId(program);
    } else {
      if (idLargerThanLastExists(program)) {
        currentId = getMinIdLargerThanPreviousId(program);
      } else {
        currentId = getMinId(program);
      }
    }
    lastChosenId = currentId;
    invocationCount++;
    return currentId;
  }

  private int getMinId(ConcurrentProgram program) {
    return program.getEnabledThreadIds().stream().min(Integer::compareTo).get();
  }

  private int getMinIdLargerThanPreviousId(ConcurrentProgram program) {
    return program.getEnabledThreadIds().stream()
            .filter(x -> x > lastChosenId).min(Integer::compareTo).get();
  }

  private boolean idLargerThanLastExists(ConcurrentProgram program) {
    return !program.getEnabledThreadIds().stream()
            .filter(id -> id > lastChosenId).collect(Collectors.toSet()).isEmpty();
  }
}
