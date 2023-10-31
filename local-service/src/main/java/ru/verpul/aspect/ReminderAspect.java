package ru.verpul.aspect;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ru.verpul.feign.TGBotFeign;
import ru.verpul.service.ReminderService;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class ReminderAspect {

    private final ReminderService reminderService;
    private final TGBotFeign tgBotFeign;

    @Pointcut("within(ru.verpul.service.ReminderService)")
    private void inReminderService() {}

    @Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
    private void transactionalMethods() {}

    @AfterReturning("inReminderService() && transactionalMethods()")
    public void afterTransactionalMethod() {
        try {
            tgBotFeign.updateTimedRemindersData(reminderService.getTimedRemindersForToday());
        } catch (FeignException e) {
            log.error("Ошибка при отправке данных о напоминаниях в tg-bot-service", e);
        }
    }
}
