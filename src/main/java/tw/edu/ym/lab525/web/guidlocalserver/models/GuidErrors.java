/// **
// *
// * @author Wei-Ming Wu
// *
// *
// * Copyright 2014 Wei-Ming Wu
// *
// * Licensed under the Apache License, Version 2.0 (the "License"); you may not
// * use this file except in compliance with the License. You may obtain a copy
/// of
// * the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
// * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
// * License for the specific language governing permissions and limitations
/// under
// * the License.
// *
// */
// package tw.edu.ym.lab525.web.guidlocalserver.models;
//
// import java.util.List;
//
// import com.google.common.base.MoreObjects;
// import com.google.common.base.Objects;
// import com.google.common.collect.ImmutableList;
//
/// **
// *
// * {@link GuidErrors} is designed to hold error messages of the GUID service.
/// It
// * is immutable.
// *
// */
// public final class GuidErrors {
//
// private final List<String> errors;
//
// /**
// * Creates a {@link GuidErrors}.
// *
// * @param errors
// * a list of error messages
// */
// public GuidErrors(List<String> errors) {
// this.errors = ImmutableList.copyOf(errors);
// }
//
// /**
// * Returns a list of error messages.
// *
// * @return a list of error messages
// */
// public List<String> getErrors() {
// return errors;
// }
//
// @Override
// public boolean equals(Object o) {
// if (o instanceof GuidErrors) {
// GuidErrors err = (GuidErrors) o;
// return Objects.equal(errors, err.errors);
// }
// return false;
// }
//
// @Override
// public int hashCode() {
// return Objects.hashCode(errors);
// }
//
// @Override
// public String toString() {
// return MoreObjects.toStringHelper(this).add("errors", errors).toString();
// }
//
// }
