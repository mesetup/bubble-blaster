import os
import time

print(os.getpid())
# time.sleep(60)

# os.kill(os.getpid(), 0xcfffffff)

from com.ultreon.bubbles.entity.core import Entity
from java.util.function import Consumer
from java.lang import String
from java.lang import Object
from java.util import Objects

from com.ultreon.bubbles.util import Fluid

entityConsumer: Consumer(Entity) = lambda entity: print(entity)
# noinspection PyTypeChecker
halloJava: Object = Objects.toString("Hallo")


class Lol(String):
    def __init__(self):
        """
        Some funny class that uses Java modules with Python syntax. Do you understand it? Me not.

        :param self itself.
        :return Nothing
        """
        String("Hallo")
        self.lower = self.toLowerCase()
        self.lol = Lol()
        self.lol.lol.lol.lol.lol.lol.lol.lol.lol.lol.lol.lol.lol.lol.lol.lol.lol.lol.lol.lol.lol.lower = 3


class Blood(Fluid):
    def __init__(self):
        print(self.error)


del Entity
