package com.dronegcs.mavlink.is.protocol.msg_metadata.enums;

public enum MAV_PARAM_GROUP_PLANE implements MAV_PARAM_GROUP_I {

    // Plane
    ARDUPLANE("ArduPlane"),
    ADSB("ADSB"),
    AFS("AFS"),
    AHRS("AHRS"),
    ARMING("ARMING"),
    ARSPD("ARSPD"),
    AVD("AVD"),
    BATT2("BATT2"),
    BATT3("BATT3"),
    BATT4("BATT4"),
    BATT5("BATT5"),
    BATT6("BATT6"),
    BATT7("BATT7"),
    BATT8("BATT8"),
    BATT9("BATT9"),
    BATT("BATT"),
    BRD("BRD"),
    BRDRADIO("BRDRADIO"),
    BRDRTC("BRDRTC"),
    BTN("BTN"),
    CAM("CAM"),
    CAND1("CAND1"),
    CAND1KDE("CAND1KDE"),
    CAND1UC("CAND1UC"),
    CAND2("CAND2"),
    CAND2KDE("CAND2KDE"),
    CAND2UC("CAND2UC"),
    CAND3("CAND3"),
    CAND3KDE("CAND3KDE"),
    CAND3UC("CAND3UC"),
    CANP1("CANP1"),
    CANP2("CANP2"),
    CANP3("CANP3"),
    CANSLCAN("CANSLCAN"),
    CHUTE("CHUTE"),
    COMPASS("COMPASS"),
    COMPASSPMOT("COMPASSPMOT"),
    EFI("EFI"),
    EK2("EK2"),
    EK3("EK3"),
    FLOW("FLOW"),
    GND("GND"),
    GPS("GPS"),
    GRIP("GRIP"),
    ICE("ICE"),
    INS("INS"),
    INSHNTCH("INSHNTCH"),
    INSLOG("INSLOG"),
    INSNOTCH("INSNOTCH"),
    LAND("LAND"),
    LANDDS("LANDDS"),
    LANDDSDS("LANDDSDS"),
    LGR("LGR"),
    LOG("LOG"),
    MIS("MIS"),
    MNT("MNT"),
    NAVL1("NAVL1"),
    NTF("NTF"),
    OSD("OSD"),
    OSD1("OSD1"),
    OSD2("OSD2"),
    OSD3("OSD3"),
    OSD4("OSD4"),
    PTCH2SRV("PTCH2SRV"),
    Q("Q"),
    QAUTOTUNE("QAUTOTUNE"),
    QA("QA"),
    QLOIT("QLOIT"),
    QM("QM"),
    QP("QP"),
    QWP("QWP"),
    RALLY("RALLY"),
    RC("RC"),
    RC10("RC10"),
    RC11("RC11"),
    RC12("RC12"),
    RC13("RC13"),
    RC14("RC14"),
    RC15("RC15"),
    RC16("RC16"),
    RC1("RC1"),
    RC2("RC2"),
    RC3("RC3"),
    RC4("RC4"),
    RC5("RC5"),
    RC6("RC6"),
    RC7("RC7"),
    RC8("RC8"),
    RC9("RC9"),
    RCMAP("RCMAP"),
    RELAY("RELAY"),
    RLL2SRV("RLL2SRV"),
    RNGFND1("RNGFND1"),
    RNGFND2("RNGFND2"),
    RNGFND3("RNGFND3"),
    RNGFND4("RNGFND4"),
    RNGFND5("RNGFND5"),
    RNGFND6("RNGFND6"),
    RNGFND7("RNGFND7"),
    RNGFND8("RNGFND8"),
    RNGFND9("RNGFND9"),
    RNGFNDA("RNGFNDA"),
    RPM("RPM"),
    RSSI("RSSI"),
    SCHED("SCHED"),
    SCR("SCR"),
    SERIAL("SERIAL"),
    SERVO("SERVO"),
    SERVO10("SERVO10"),
    SERVO11("SERVO11"),
    SERVO12("SERVO12"),
    SERVO13("SERVO13"),
    SERVO14("SERVO14"),
    SERVO15("SERVO15"),
    SERVO16("SERVO16"),
    SERVO1("SERVO1"),
    SERVO2("SERVO2"),
    SERVO3("SERVO3"),
    SERVO4("SERVO4"),
    SERVO5("SERVO5"),
    SERVO6("SERVO6"),
    SERVO7("SERVO7"),
    SERVO8("SERVO8"),
    SERVO9("SERVO9"),
    SERVOBLH("SERVOBLH"),
    SERVOROB("SERVOROB"),
    SERVOSBUS("SERVOSBUS"),
    SERVOVOLZ("SERVOVOLZ"),
    SIMGRPE("SIMGRPE"),
    SIMGRPS("SIMGRPS"),
    SIMPLD("SIMPLD"),
    SIMSPR("SIMSPR"),
    SOAR("SOAR"),
    SR0("SR0"),
    SR1("SR1"),
    SR2("SR2"),
    SR3("SR3"),
    STAT("STAT"),
    STEER2SRV("STEER2SRV"),
    TECS("TECS"),
    TERRAIN("TERRAIN"),
    TKOFF("TKOFF"),
    TUNE("TUNE"),
    YAW2SRV("YAW2SRV"),

    ;

    private final String name;

    MAV_PARAM_GROUP_PLANE(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}