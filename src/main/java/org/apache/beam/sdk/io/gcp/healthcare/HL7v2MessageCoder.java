// Copyright 2020 Google LLC.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.beam.sdk.io.gcp.healthcare;

import com.google.api.services.healthcare.v1beta1.model.Message;
import com.google.api.services.healthcare.v1beta1.model.ParsedData;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import org.apache.beam.sdk.coders.CoderException;
import org.apache.beam.sdk.coders.CustomCoder;
import org.apache.beam.sdk.coders.MapCoder;
import org.apache.beam.sdk.coders.NullableCoder;
import org.apache.beam.sdk.coders.StringUtf8Coder;

/** Coder for encoding and decoding a HL7v2 message. */
public class HL7v2MessageCoder extends CustomCoder<HL7v2Message> {
  private static final NullableCoder<String> STRING_CODER = NullableCoder.of(StringUtf8Coder.of());
  private static final NullableCoder<Map<String, String>> MAP_CODER =
      NullableCoder.of(MapCoder.of(STRING_CODER, STRING_CODER));
  private static final NullableCoder<ParsedData> PARSED_DATA_CODER =
      NullableCoder.of(ParsedDataCoder.of());

  public static HL7v2MessageCoder of() {
    return new HL7v2MessageCoder();
  }

  public static HL7v2MessageCoder of(Class<HL7v2Message> clazz) {
    return new HL7v2MessageCoder();
  }

  @Override
  public void encode(HL7v2Message value, OutputStream outStream) throws IOException {
    STRING_CODER.encode(value.getName(), outStream);
    STRING_CODER.encode(value.getMessageType(), outStream);
    STRING_CODER.encode(value.getCreateTime(), outStream);
    STRING_CODER.encode(value.getSendTime(), outStream);
    STRING_CODER.encode(value.getData(), outStream);
    STRING_CODER.encode(value.getSendFacility(), outStream);
    MAP_CODER.encode(value.getLabels(), outStream);
    PARSED_DATA_CODER.encode(value.getParsedData(), outStream);
    STRING_CODER.encode(value.getSchematizedData(), outStream);
  }

  @Override
  public HL7v2Message decode(InputStream inStream) throws CoderException, IOException {
    Message msg = new Message();
    msg.setName(STRING_CODER.decode(inStream));
    msg.setMessageType(STRING_CODER.decode(inStream));
    msg.setCreateTime(STRING_CODER.decode(inStream));
    msg.setSendTime(STRING_CODER.decode(inStream));
    msg.setData(STRING_CODER.decode(inStream));
    msg.setSendFacility(STRING_CODER.decode(inStream));
    msg.setLabels(MAP_CODER.decode(inStream));
    HL7v2Message out = HL7v2Message.fromModel(msg);
    out.setParsedData(PARSED_DATA_CODER.decode(inStream));
    out.setSchematizedData(STRING_CODER.decode(inStream));
    return out;
  }
}
