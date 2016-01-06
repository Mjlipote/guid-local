/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Wei-Ming Wu
 *
 */
package tw.guid.local.util;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static tw.guid.local.util.ComboLastName.hasComboLastName;

import tw.guid.client.field.Name;

/**
 * 
 * NameSplitter splits Chinese name into first name & last name.
 * 
 */
public final class NameSplitter {

  private NameSplitter() {}

  /**
   * Creates a Name.
   * 
   * @param name
   *          a Chinese name
   * @return a Name
   */
  public static Name split(String name) {
    name = checkNotNull(name).replaceAll("\\s+", " ").trim();
    checkArgument(name.replaceAll("\\s+", "").length() >= 2, "Name too short.");

    String[] fullName;
    if ((fullName = name.split(" ")).length == 2)
      return new Name(fullName[0], fullName[1]);

    if ((fullName = name.split("・")).length == 2)
      return new Name(fullName[1], fullName[0]);

    if (name.length() == 4)
      return new Name(name.substring(2), name.substring(0, 2));

    if (hasComboLastName(name))
      return new Name(name.substring(2, name.length()), name.substring(0, 2));
    else
      return new Name(name.substring(1, name.length()), name.substring(0, 1));
  }

}
