# AcidRain
[![](http://i.loli.net/2018/10/23/5bcef3bf626e3.png)](http://www.mcbbs.net/thread-825299-1-1.html "酸雨")

Acid rain effect plugin for Nukkit

Please see [mcbbs](http://www.mcbbs.net/thread-825299-1-1.html) for more information.
## Permissions
| Permission | Description |
| - | - |
| acidrain.immune | Grants immunity to effect from acid rain |
## config.yml
```yaml
# seconds
delay: 5
world:
  - "world"
immune-armor:
  - 298
  - 136
  - 314
effects:
  -
    id: 2
    # seconds
    duration: 6
    amplifier: 2
  -
    id: 9
    duration: 8
    amplifier: 0
  -
    id: 17
    duration: 10
    amplifier: 1
  -
    id: 18
    duration: 9
    amplifier: 1
  -
    id: 19
    duration: 5
    amplifier: 0
```
