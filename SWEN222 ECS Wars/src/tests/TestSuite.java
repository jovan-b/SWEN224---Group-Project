package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	CharacterTests.class,
	ContainerTests.class,
	RoomTests.class,
	SaveLoadTests.class
})

public class TestSuite {
}