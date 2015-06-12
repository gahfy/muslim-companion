package net.gahfy.muslimcompanion.utils;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import net.gahfy.muslimcompanion.MainActivity;
import net.gahfy.muslimcompanion.R;
import net.gahfy.muslimcompanion.models.MuslimLocation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class AlarmUtils extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        notifyAndSetNextAlarm(context, true);
    }

    public static void notifyAndSetNextAlarm(Context context, boolean shouldNotify){
        shouldNotify = shouldNotify && SharedPreferencesUtils.getNotifyPrayer(context);
        Uri fajrAdhanUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.adhan_fajr);
        Uri normalAdhanUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.adhan_normal);

        Uri soundUri = null;

        if(SharedPreferencesUtils.getSoundNotificationPrayer(context) == 1)
            soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        MuslimLocation muslimLocation = SharedPreferencesUtils.getLastLocation(context);
        if(muslimLocation != null) {
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTimeInMillis(new Date().getTime());

            int day = gregorianCalendar.get(Calendar.DAY_OF_MONTH);
            int month = gregorianCalendar.get(Calendar.MONTH)+1;
            int year = gregorianCalendar.get(Calendar.YEAR);

            PrayerTimesUtils prayerTimesUtils = new PrayerTimesUtils(context, year, month, day, muslimLocation.getLocationLatitude(), muslimLocation.getLocationLongitude(), PrayerTimesUtils.Convention.MUSLIM_WORLD_LEAGUE, PrayerTimesUtils.School.NOT_HANAFI);
            boolean countryHasBeenChanged = false;

            String[] locationDatas = LocationUtils.getCountryIsoAndCityName(context, muslimLocation);

            String countryIso = LocationUtils.getCountryIso(context);
            if(countryIso != null){
                prayerTimesUtils.changeCountry(countryIso.toLowerCase());
                countryHasBeenChanged = true;
            }
            if(!countryHasBeenChanged){
                if(locationDatas != null) {
                    if(locationDatas[0] != null) {
                        prayerTimesUtils.changeCountry(locationDatas[0].toLowerCase());
                    }
                }
            }

            long currentTimestamp = new Date().getTime();
            long currentTimestampMinus10Minutes = currentTimestamp - (35l*60000l);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(context.getString(R.string.hour_format), Locale.getDefault());

            long timestampOfNextAlarm = 0l;

            String cityName = "";
            if(locationDatas != null) {
                if(locationDatas[1] != null)
                cityName = locationDatas[1];
            }
            String notificationTitle = "";
            String notificationContent = "";

            if(prayerTimesUtils.getFajrTimestampOfNextDay() <  currentTimestamp && prayerTimesUtils.getFajrTimestampOfNextDay() > currentTimestampMinus10Minutes){
                if(prayerTimesUtils.getIshaTimestamp() == prayerTimesUtils.getFajrTimestampOfNextDay()){
                    // Isha and Fajr of next day
                    timestampOfNextAlarm = prayerTimesUtils.getDhuhrOfNextDayTimestamp();
                    notificationTitle = context.getString(R.string.prayer_name_isha_and_fajr);

                    notificationContent = context.getString(R.string.prayer_notification_content,
                            context.getString(R.string.prayer_name_isha_and_fajr),
                            simpleDateFormat.format(new Date(prayerTimesUtils.getIshaTimestamp())),
                            cityName);
                    shouldNotify = shouldNotify && (prayerTimesUtils.getIshaTimestamp() != SharedPreferencesUtils.getLastNotificationPrayer(context));
                    SharedPreferencesUtils.putLastNotificationPrayer(context, prayerTimesUtils.getIshaTimestamp());
                    if(SharedPreferencesUtils.getSoundNotificationPrayer(context) == 2){
                        soundUri = normalAdhanUri;
                    }
                }
                else{
                    // Fajr of next day
                    timestampOfNextAlarm = prayerTimesUtils.getDhuhrOfNextDayTimestamp();
                    notificationTitle = context.getString(R.string.prayer_name_fajr);

                    notificationContent = context.getString(R.string.prayer_notification_content,
                            context.getString(R.string.prayer_name_fajr),
                            simpleDateFormat.format(new Date(prayerTimesUtils.getFajrTimestampOfNextDay())),
                            cityName);
                    shouldNotify = shouldNotify && (prayerTimesUtils.getFajrTimestampOfNextDay() != SharedPreferencesUtils.getLastNotificationPrayer(context));
                    SharedPreferencesUtils.putLastNotificationPrayer(context, prayerTimesUtils.getFajrTimestampOfNextDay());
                    if(SharedPreferencesUtils.getSoundNotificationPrayer(context) == 2) {
                        soundUri = fajrAdhanUri;
                    }
                }
            }
            else if(prayerTimesUtils.getIshaTimestamp() < currentTimestamp && prayerTimesUtils.getIshaTimestamp() > currentTimestampMinus10Minutes){
                // Isha
                timestampOfNextAlarm = prayerTimesUtils.getFajrTimestampOfNextDay();
                notificationTitle = context.getString(R.string.prayer_name_isha);

                notificationContent = context.getString(R.string.prayer_notification_content,
                        context.getString(R.string.prayer_name_isha),
                        simpleDateFormat.format(new Date(prayerTimesUtils.getIshaTimestamp())),
                        cityName);
                shouldNotify = shouldNotify && (prayerTimesUtils.getIshaTimestamp() != SharedPreferencesUtils.getLastNotificationPrayer(context));
                SharedPreferencesUtils.putLastNotificationPrayer(context, prayerTimesUtils.getIshaTimestamp());
                if(SharedPreferencesUtils.getSoundNotificationPrayer(context) == 2) {
                    soundUri = normalAdhanUri;
                }
            }
            else if(prayerTimesUtils.getMaghribTimestamp() < currentTimestamp && prayerTimesUtils.getMaghribTimestamp() > currentTimestampMinus10Minutes){
                // Maghrib
                timestampOfNextAlarm = prayerTimesUtils.getIshaTimestamp();
                notificationTitle = context.getString(R.string.prayer_name_maghrib);

                notificationContent = context.getString(R.string.prayer_notification_content,
                        context.getString(R.string.prayer_name_maghrib),
                        simpleDateFormat.format(new Date(prayerTimesUtils.getMaghribTimestamp())),
                        cityName);
                shouldNotify = shouldNotify && (prayerTimesUtils.getMaghribTimestamp() != SharedPreferencesUtils.getLastNotificationPrayer(context));
                SharedPreferencesUtils.putLastNotificationPrayer(context, prayerTimesUtils.getMaghribTimestamp());
                if(SharedPreferencesUtils.getSoundNotificationPrayer(context) == 2) {
                    soundUri = normalAdhanUri;
                }
            }
            else if(prayerTimesUtils.getAsrTimestamp() < currentTimestamp && prayerTimesUtils.getAsrTimestamp() > currentTimestampMinus10Minutes){
                // Asr
                timestampOfNextAlarm = prayerTimesUtils.getMaghribTimestamp();
                notificationTitle = context.getString(R.string.prayer_name_asr);

                notificationContent = context.getString(R.string.prayer_notification_content,
                        context.getString(R.string.prayer_name_asr),
                        simpleDateFormat.format(new Date(prayerTimesUtils.getAsrTimestamp())),
                        cityName);
                shouldNotify = shouldNotify && (prayerTimesUtils.getAsrTimestamp() != SharedPreferencesUtils.getLastNotificationPrayer(context));
                SharedPreferencesUtils.putLastNotificationPrayer(context, prayerTimesUtils.getAsrTimestamp());
                if(SharedPreferencesUtils.getSoundNotificationPrayer(context) == 2) {
                    soundUri = normalAdhanUri;
                }
            }

            else if(prayerTimesUtils.getDhuhrTimestamp() < currentTimestamp && prayerTimesUtils.getDhuhrTimestamp() > currentTimestampMinus10Minutes){
                // Dhuhr
                timestampOfNextAlarm = prayerTimesUtils.getAsrTimestamp();
                if(prayerTimesUtils.isFriday())
                    notificationTitle = context.getString(R.string.prayer_name_jumuah);
                else
                    notificationTitle = context.getString(R.string.prayer_name_dhuhr);

                notificationContent = context.getString(R.string.prayer_notification_content,
                        notificationTitle,
                        simpleDateFormat.format(new Date(prayerTimesUtils.getDhuhrTimestamp())),
                        cityName);
                shouldNotify = shouldNotify && (prayerTimesUtils.getDhuhrTimestamp() != SharedPreferencesUtils.getLastNotificationPrayer(context));
                SharedPreferencesUtils.putLastNotificationPrayer(context, prayerTimesUtils.getDhuhrTimestamp());
                if(SharedPreferencesUtils.getSoundNotificationPrayer(context) == 2) {
                    soundUri = normalAdhanUri;
                }
            }

            else if(prayerTimesUtils.getFajrTimestamp() < currentTimestamp && prayerTimesUtils.getFajrTimestamp() > currentTimestampMinus10Minutes){
                if(prayerTimesUtils.getIshaTimestampOfPreviousDay() == prayerTimesUtils.getFajrTimestamp()){
                    // Isha of previous day and Fajr
                    timestampOfNextAlarm = prayerTimesUtils.getDhuhrTimestamp();
                    notificationTitle = context.getString(R.string.prayer_name_isha_and_fajr);

                    notificationContent = context.getString(R.string.prayer_notification_content,
                            context.getString(R.string.prayer_name_isha_and_fajr),
                            simpleDateFormat.format(new Date(prayerTimesUtils.getFajrTimestamp())),
                            cityName);
                    shouldNotify = shouldNotify && (prayerTimesUtils.getFajrTimestamp() != SharedPreferencesUtils.getLastNotificationPrayer(context));
                    SharedPreferencesUtils.putLastNotificationPrayer(context, prayerTimesUtils.getFajrTimestamp());
                    if(SharedPreferencesUtils.getSoundNotificationPrayer(context) == 2) {
                        soundUri = normalAdhanUri;
                    }
                }
                else{
                    // Fajr
                    timestampOfNextAlarm = prayerTimesUtils.getDhuhrTimestamp();
                    notificationTitle = context.getString(R.string.prayer_name_fajr);

                    notificationContent = context.getString(R.string.prayer_notification_content,
                            context.getString(R.string.prayer_name_fajr),
                            simpleDateFormat.format(new Date(prayerTimesUtils.getFajrTimestamp())),
                            cityName);
                    shouldNotify = shouldNotify && (prayerTimesUtils.getFajrTimestamp() != SharedPreferencesUtils.getLastNotificationPrayer(context));
                    SharedPreferencesUtils.putLastNotificationPrayer(context, prayerTimesUtils.getFajrTimestamp());
                    if(SharedPreferencesUtils.getSoundNotificationPrayer(context) == 2) {
                        soundUri = fajrAdhanUri;
                    }
                }
            }
            else if(prayerTimesUtils.getIshaTimestampOfPreviousDay() < currentTimestamp && prayerTimesUtils.getIshaTimestampOfPreviousDay() > currentTimestampMinus10Minutes){
                // Isha of previous day
                timestampOfNextAlarm = prayerTimesUtils.getFajrTimestamp();
                notificationTitle = context.getString(R.string.prayer_name_isha);

                notificationContent = context.getString(R.string.prayer_notification_content,
                        context.getString(R.string.prayer_name_isha),
                        simpleDateFormat.format(new Date(prayerTimesUtils.getIshaTimestampOfPreviousDay())),
                        cityName);
                shouldNotify = shouldNotify && (prayerTimesUtils.getIshaTimestampOfPreviousDay() != SharedPreferencesUtils.getLastNotificationPrayer(context));
                SharedPreferencesUtils.putLastNotificationPrayer(context, prayerTimesUtils.getIshaTimestampOfPreviousDay());
                if(SharedPreferencesUtils.getSoundNotificationPrayer(context) == 2) {
                    soundUri = normalAdhanUri;
                }
            }
            else{
                shouldNotify = false;
            }


            if(shouldNotify) {
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.ic_notification_small)
                                .setContentTitle(notificationTitle)
                                .setContentText(notificationContent)
                                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                                .setAutoCancel(true);
                if(soundUri != null)
                    mBuilder.setSound(soundUri);

                Intent resultIntent = new Intent(context, MainActivity.class);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(35, mBuilder.build());
            }

            if(timestampOfNextAlarm == 0){
                if (prayerTimesUtils.getIshaTimestampOfPreviousDay() > currentTimestamp) {
                    timestampOfNextAlarm = prayerTimesUtils.getIshaTimestampOfPreviousDay();
                } else if (prayerTimesUtils.getFajrTimestamp() > currentTimestamp) {
                    timestampOfNextAlarm = prayerTimesUtils.getFajrTimestamp();
                } else if (prayerTimesUtils.getDhuhrTimestamp() > currentTimestamp) {
                    timestampOfNextAlarm = prayerTimesUtils.getDhuhrTimestamp();
                } else if (prayerTimesUtils.getAsrTimestamp() > currentTimestamp) {
                    timestampOfNextAlarm = prayerTimesUtils.getAsrTimestamp();
                } else if (prayerTimesUtils.getMaghribTimestamp() > currentTimestamp) {
                    timestampOfNextAlarm = prayerTimesUtils.getMaghribTimestamp();
                } else if (prayerTimesUtils.getIshaTimestamp() > currentTimestamp) {
                    timestampOfNextAlarm = prayerTimesUtils.getIshaTimestamp();
                } else if (prayerTimesUtils.getFajrTimestampOfNextDay() > currentTimestamp) {
                    timestampOfNextAlarm = prayerTimesUtils.getFajrTimestampOfNextDay();
                } else{
                    timestampOfNextAlarm = prayerTimesUtils.getDhuhrOfNextDayTimestamp();
                }
            }


            Intent alarmIntent = new Intent(context, AlarmUtils.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, alarmIntent, 0);

            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            manager.cancel(pendingIntent);
            setAlarm(manager, timestampOfNextAlarm, pendingIntent);

        }
    }

    private static void setAlarm(AlarmManager am, long timestamp, PendingIntent pendingIntent){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            setAlarmAfter19(am, timestamp, pendingIntent);
        }
        else{
            setAlarmBefore19(am, timestamp, pendingIntent);
        }
    }

    @TargetApi(value = 19)
    private static void setAlarmAfter19(AlarmManager am, long timestamp, PendingIntent pendingIntent){
        am.setExact(AlarmManager.RTC_WAKEUP, timestamp, pendingIntent);
    }

    private static void setAlarmBefore19(AlarmManager am, long timestamp, PendingIntent pendingIntent){
        am.set(AlarmManager.RTC_WAKEUP, timestamp, pendingIntent);
    }
}
