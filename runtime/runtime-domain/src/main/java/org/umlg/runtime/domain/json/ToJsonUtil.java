package org.umlg.runtime.domain.json;

import org.umlg.runtime.domain.DataTypeEnum;
import org.umlg.runtime.domain.PersistentObject;
import org.umlg.runtime.domain.UmlgApplicationNode;
import org.umlg.runtime.util.UmlgFormatter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

public class ToJsonUtil {

    public static void main(String[] args) {
        System.out.println("restAndJson/humans/{humanId}".replaceAll("\\{(\\s*?.*?)*?\\}", String.valueOf(5)));
    }

    public static String enumsToJson(Collection<? extends Enum> enums) {
        StringBuilder json = new StringBuilder();
        if (enums != null) {
            int count = 0;
            for (Enum p : enums) {
                count++;
                json.append("\"");
                json.append(p.name());
                json.append("\"");
                if (count != enums.size()) {
                    json.append(",");
                }
            }
        }
        return json.toString();
    }

    public static <T> String primitivesToJson(Collection<T> primitives) {
        StringBuilder json = new StringBuilder();
        if (primitives != null) {
            json.append("[");
            int count = 0;
            for (T p : primitives) {
                count++;
                if (p instanceof String) {
                    json.append("\"");
                }
                json.append(p.toString());
                if (p instanceof String) {
                    json.append("\"");
                }
                if (count != primitives.size()) {
                    json.append(",");
                }
            }
            json.append("]");
        } else {
            json.append("null");
        }
        return json.toString();
    }

    public static String toJson(Collection<? extends PersistentObject> entities) {
        return toJson(entities, false);
    }

    public static String toJson(Collection<? extends PersistentObject> entities, boolean deep) {
        StringBuilder json = new StringBuilder();
        if (entities != null) {
            int count = 0;
            for (PersistentObject p : entities) {
                count++;
                json.append(p.toJson(deep));
                if (count != entities.size()) {
                    json.append(",");
                }
            }
        }
        return json.toString();
    }

    public static String toJsonLocalDate(Collection<LocalDate> localDates) {
        StringBuilder json = new StringBuilder();
        if (localDates != null) {
            json.append("[");
            int count = 0;
            for (LocalDate p : localDates) {
                count++;
                json.append("\"");
                json.append(UmlgFormatter.format(DataTypeEnum.Date, p));
                json.append("\"");
                if (count != localDates.size()) {
                    json.append(",");
                }
            }
            json.append("]");
        } else {
            json.append("null");
        }
        return json.toString();


    }

    public static String toJsonDateTime(Collection<LocalDateTime> dateTimes) {
        StringBuilder json = new StringBuilder();
        if (dateTimes != null) {
            json.append("[");
            int count = 0;
                for (LocalDateTime p : dateTimes) {
                count++;
                json.append("\"");
                json.append(UmlgFormatter.format(DataTypeEnum.DateTime, p));
                json.append("\"");
                if (count != dateTimes.size()) {
                    json.append(",");
                }
            }
            json.append("]");
        } else {
            json.append("null");
        }
        return json.toString();
    }

    public static String toJsonLocalTime(Collection<LocalTime> localTimes) {
        StringBuilder json = new StringBuilder();
        if (localTimes != null) {
            json.append("[");
            int count = 0;
            for (LocalTime localTime : localTimes) {
                count++;
                json.append("\"");
                json.append(UmlgFormatter.format(DataTypeEnum.Time, localTime));
                json.append("\"");
                if (count != localTimes.size()) {
                    json.append(",");
                }
            }
            json.append("]");
        } else {
            json.append("null");
        }
        return json.toString();
    }

    public static String toJsonWithoutCompositeParentForQuery(Collection<? extends PersistentObject> entities) {
        StringBuilder json = new StringBuilder();
        if (entities != null) {
            int count = 0;
            for (PersistentObject p : entities) {
                count++;
                json.append(p.toJsonWithoutCompositeParent());
                if (count != entities.size()) {
                    json.append(",");
                }
            }
        }
        return json.toString();
    }

    public static String toJsonWithoutCompositeParent(Collection<? extends PersistentObject> entities) {
        return toJsonWithoutCompositeParent(entities, false);
    }

    public static String toJsonWithoutCompositeParent(Collection<? extends PersistentObject> entities, boolean deep) {
        StringBuilder json = new StringBuilder();
        if (entities != null) {
            int count = 0;
            for (PersistentObject p : entities) {
                count++;
                json.append(p.toJsonWithoutCompositeParent(deep));
                if (count != entities.size()) {
                    json.append(",");
                }
            }
        }
        return json.toString();
    }

    public static String toJson(PersistentObject entity) {
        return toJson(entity, false);
    }

    public static String toJson(PersistentObject entity, boolean deep) {
        if (entity != null) {
            StringBuilder json = new StringBuilder();
            json.append(entity.toJson(deep));
            return json.toString();
        } else {
            return "";
        }
    }

    public static String toJson(UmlgApplicationNode entity) {
        if (entity != null) {
            StringBuilder json = new StringBuilder();
            json.append(entity.toJson());
            return json.toString();
        } else {
            return "";
        }
    }

    public static String toJsonWithoutCompositeParent(PersistentObject entity) {
        if (entity != null) {
            StringBuilder json = new StringBuilder();
            json.append(entity.toJsonWithoutCompositeParent());
            return json.toString();
        } else {
            return "null";
        }
    }
}
