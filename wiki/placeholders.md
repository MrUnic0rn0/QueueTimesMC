[&laquo; Back](README.md)
# :memo: Placeholders

**Attention:**  
Only works with active parks (use `/qt park <id_park> set true` to add an active park)

 - `%qt_<id_park>_<id_ride>%` : Default Queue Time ( Open -> queue_time + config.min , Closed -> config.default_time + config.min )
 - `%qt_<id_park>_<id_ride>_name%` : Ride's name
 - `%qt_<id_park>_<id_ride>_time%` : Real Queue Time ( queue_time + config.min )
 - `%qt_<id_park>_<id_ride>_status%` : Real Queue Time ( Open -> config.open , Closed -> config.close )
