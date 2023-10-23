package ru.verpul.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ru.verpul.feign.TGBotFeign;
import ru.verpul.service.ReminderService;

@Component
@Aspect
@RequiredArgsConstructor
public class ReminderAspect {

    private final ReminderService reminderService;
    private final TGBotFeign tgBotFeign;

    @Pointcut("within(ru.verpul.service.ReminderService)")
    private void inReminderService() {}

    @Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
    private void transactionalMethods() {}

    @AfterReturning("inReminderService() && transactionalMethods()")
    public void afterTransactionalMethod() {
        tgBotFeign.updateTimedRemindersData(reminderService.getTimedRemindersForToday());
    }
}
