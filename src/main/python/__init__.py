import os

print(os.getpid())
# time.sleep(60)

# os.kill(os.getpid(), 0xcfffffff)

from qtech.bubbles.common.entity import Entity
from kotlin.util.function import Consumer
from kotlin.lang import String
from kotlin.lang import Object
from kotlin.util import Objects

from qtech.bubbles.util import Fluid

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
