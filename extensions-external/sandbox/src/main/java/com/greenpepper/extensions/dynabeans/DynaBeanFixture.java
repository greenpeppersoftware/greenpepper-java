package com.greenpepper.extensions.dynabeans;

import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.Message;
import com.greenpepper.reflect.NoSuchMessageException;

public class DynaBeanFixture implements Fixture {
    private final Fixture fixture;

    public DynaBeanFixture(Fixture decorated) {
        this.fixture = decorated;
    }

    public boolean canSend(String message) {
        return fixture.canSend(message) || DynaBeanUtil.hasProperty(getTarget(), message);
    }

    public boolean canCheck(String message) {
        return fixture.canCheck(message) || DynaBeanUtil.hasProperty(getTarget(), message);
    }

    public Message check(String message) throws NoSuchMessageException {
        if (fixture.canCheck(message)) return fixture.check(message);
        if (!DynaBeanUtil.hasProperty(getTarget(), message)) throw new NoSuchMessageException(message);

        return new DynaPropertyReader(DynaBeanUtil.asDynaBean(getTarget()), message);
    }

    public Object getTarget() {
        return fixture.getTarget();
    }

    public Message send(String message) throws NoSuchMessageException {
        if (fixture.canSend(message)) return fixture.send(message);
        if (!DynaBeanUtil.hasProperty(getTarget(), message)) throw new NoSuchMessageException(message);

        return new DynaPropertyWriter(DynaBeanUtil.asDynaBean(getTarget()), message);
    }

    public Message getter(String message) throws NoSuchMessageException {
        return check(message);
    }

    public Fixture fixtureFor(Object target) {
        return new DynaBeanFixture(fixture.fixtureFor(target));    
    }
}
