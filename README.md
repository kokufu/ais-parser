# AIS Parser
This is a simple library for parsing [AIS](https://en.wikipedia.org/wiki/Automatic_identification_system) (Automatic Identification System) record.

- You can parse each sentence which start with "!AIVDM" or "!AIVDO" to AisRecord instance
- Repeat sentences can be parsed
- The implemented AisRecords as it now stands are below
  - [Position Report Class A](http://catb.org/gpsd/AIVDM.html#_types_1_2_and_3_position_report_class_a) (type 1, 2, 3)
  - [Static and Voyage Related Data](http://catb.org/gpsd/AIVDM.html#_type_5_static_and_voyage_related_data) (type 5)
  - [Standard Class B CS Position Report](http://catb.org/gpsd/AIVDM.html#_type_18_standard_class_b_cs_position_report) (type 18)
  - [Static Data Report](http://catb.org/gpsd/AIVDM.html#_type_24_static_data_report) (type 24)
- You can create your custom AisParser for unimplemented type



## How to use
### Install

### Parse
To parse AIS record, instantiate `AisParser` and call `AisParser#parse(String)` for each line.

The simplest example is shown below.

```java
AisParser aisParser = new AisParser();

BufferedReader reader = null;
try {
    // InputStream (is) must be set and initialized before
    reader = new BufferedReader(new InputStreamReader(is));

    String line;
    while ((line = reader.readLine()) != null) {
        try {
            AisRecord record = (AisRecord) aisParser.parse(line);
            if (record != null) {
                // Notify record is updated
                // callback.onRecordUpdated(record);
            }
        } catch (NoParserException e) {
            // Do nothing
        } catch (InvalidDataFormatException e) (
            // Do something
        }
    }
} catch (IOException e) {
    // Do something
} finally {
    try {
        if (reader != null) {
            reader.close();
        }
    } catch (IOException e) {
        // Do something
    }
}
```

You can cast the each `AisRecord` instance to the specific class like below.

```java
void onRecordUpdated(AisRecord record) {
    if (record instanceof AisPositionReportClassARecord) {
        AisPositionReportClassARecord r = (AisPositionReportClassARecord) record;
        mSpeedOverGround = r.getSpeedOverGround();
        mPositionAccuracy = r.getPositionAccuracy();
        mLongitude = r.getLongitude();
        mLatitude = r.getLatitude();
        mCourseOverGround = r.getCourseOverGround();
        mTrueHeading = r.getTrueHeading();
        mTimeStamp = r.getTimeStamp();
    } else if (record instanceof AisPositionReportClassBRecord) {
        AisPositionReportClassBRecord r = (AisPositionReportClassBRecord) record;
        mSpeedOverGround = r.getSpeedOverGround();
        mPositionAccuracy = r.getPositionAccuracy();
        mLongitude = r.getLongitude();
        mLatitude = r.getLatitude();
        mCourseOverGround = r.getCourseOverGround();
        mTrueHeading = r.getTrueHeading();
        mTimeStamp = r.getTimeStamp();
    } else if (record instanceof AisStaticAndVoyageRelatedDataRecord) {
        AisStaticAndVoyageRelatedDataRecord r = (AisStaticAndVoyageRelatedDataRecord) record;
        mName = r.getName();
        mDimensionToBow = r.getDimensionToBow();
        mDimensionToStern = r.getDimensionToStern();
        mDimensionToPort = r.getDimensionToPort();
        mDimensionToStarboard = r.getDimensionToStarboard();
        mDestination = r.getDestination();
    } else if (record instanceof AisStaticDataReportRecord) {
        AisStaticDataReportRecord r = (AisStaticDataReportRecord) record;
        if (r.getPartType() == AisStaticDataReportRecord.PartType.A) {
            mName = r.getName();
        } else if (r.getPartType() == AisStaticDataReportRecord.PartType.B) {
            mDimensionToBow = r.getDimensionToBow();
            mDimensionToStern = r.getDimensionToStern();
            mDimensionToPort = r.getDimensionToPort();
            mDimensionToStarboard = r.getDimensionToStarboard();
        }
    }
}
```

## Custom parser
If you want to parse the record which has not been implemented yet,
you can create a subclass of `AisRecord` and set it as custom record.

Below is an example to set "Base Station Report" record known as "Type 4."
```java
AisParser aisParser = new AisParser();

// You must create BaseStationReportRecord class before
BaseStationReportRecord r = new BaseStationReportRecord();

// Set the record as custom record whose type is 4
aisParser.setCustomRecord(4, r);

// Call aisParser.parse(String) for each line
```

To implement "Base Station Report",
only you have to do is create `Segment`s array and the getter method like below.
It's better to see the real implementation of `AisRecord` like `AisStaticAndVoyageRelatedDataRecord`.

```java
public class BaseStationReportRecord extends AisRecord {
    private final Segment[] mSegments = {
            new Segment(38, 78, SegmentConverters.TO_FULL_CALENDAR), // Time
            new Segment(78, 79, SegmentConverters.TO_UNSIGNED_INT), // Accuracy
            // ellipsis
    };

    public int getAccuracy() {
        return (int) mSegments[1].getValue();
    }

    // ellipsis
}
```

When the new record works fine, please make a pull request ;-)
