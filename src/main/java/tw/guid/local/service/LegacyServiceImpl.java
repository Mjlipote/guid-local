/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local.service;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import tw.guid.central.core.PrefixedHashBundle;
import tw.guid.central.core.PublicGuid;
import tw.guid.local.entity.AccountUser;
import tw.guid.local.entity.SubprimeGuid;
import tw.guid.local.helper.HashcodeCreator;
import tw.guid.local.model.GuidHashClient;
import tw.guid.local.repository.AccountUsersRepository;
import tw.guid.local.repository.SubprimeGuidRepository;
import tw.guid.local.web.LegacyGuidException;

public class LegacyServiceImpl implements LegacyService {

  private static final Logger log =
      LoggerFactory.getLogger(LegacyServiceImpl.class);

  private final GuidHashClient guidClient;

  public LegacyServiceImpl(String centralServer, String clientKey)
      throws URISyntaxException {
    guidClient = new GuidHashClient(new URI(centralServer), clientKey);
  }

  @Autowired
  private AccountUsersRepository acctUserRepo;

  @Autowired
  private SubprimeGuidRepository subprimeGuidRepo;

  @Override
  public String authenticate(HttpServletRequest request) {
    return new Gson().toJson(isValidate(request), Boolean.class);
  }

  @Override
  public String create(String prefix, String jsonHashes,
      HttpServletRequest request) {
    if (!isValidate(request)) throw new LegacyGuidException(UNAUTHORIZED);

    if (prefix.equals("") && isValidate(request)) {
      prefix = getPrefixFromCurrentLoginUser(request);
    } else if (prefix.equals("")) {
      prefix = "PSEUDO";
    }

    List<String> correctGuids = newArrayList();
    for (PrefixedHashBundle phb : buildRequests(prefix, jsonHashes)) {
      // System.out.println(phb);
      try {
        SubprimeGuid spGuid =
            subprimeGuidRepo.findByHashcode1AndHashcode2AndHashcode3AndPrefix(
                phb.getHash1(), phb.getHash2(), phb.getHash3(), prefix);
        if (spGuid != null) {
          correctGuids.add(spGuid.getSpguid());
        } else {
          PublicGuid guid = guidClient.compute(phb);
          String spguid = guid.getPrefix() + "-" + guid.getCode();
          correctGuids.add(spguid);
          SubprimeGuid subprimeGuid = new SubprimeGuid();
          subprimeGuid.setSpguid(spguid);
          subprimeGuid.setPrefix(guid.getPrefix());
          subprimeGuid.setHashcode1(phb.getHash1().substring(0, 128));
          subprimeGuid.setHashcode2(phb.getHash2().substring(0, 128));
          subprimeGuid.setHashcode3(phb.getHash3().substring(0, 128));
          subprimeGuid.setCreatedAt(Calendar.getInstance().getTime());
          subprimeGuidRepo.save(subprimeGuid);
        }
      } catch (IOException e) {
        log.error(null, e);
        throw new LegacyGuidException(INTERNAL_SERVER_ERROR, e.getMessage());
      }
    }

    return new Gson().toJson(correctGuids);
  }

  private List<PrefixedHashBundle> buildRequests(String prefix,
      String jsonHashes) {
    List<PrefixedHashBundle> prefixedHashBundles = newArrayList();

    List<String> hash = null;
    List<List<String>> hashes = null;
    try {
      hash = new Gson().fromJson(jsonHashes,
          new TypeToken<List<String>>() {}.getType());
    } catch (Exception e) {
      hashes = new Gson().fromJson(jsonHashes,
          new TypeToken<List<List<String>>>() {}.getType());
    }

    if (hash != null) {
      prefixedHashBundles.add(new PrefixedHashBundle(prefix,
          hash.get(0).substring(0, 128).toUpperCase(),
          hash.get(1).substring(0, 128).toUpperCase(),
          hash.get(2).substring(0, 128).toUpperCase()));
    }
    if (hashes != null) {
      for (List<String> h : hashes) {
        prefixedHashBundles.add(new PrefixedHashBundle(prefix,
            h.get(0).substring(0, 128).toUpperCase(),
            h.get(1).substring(0, 128).toUpperCase(),
            h.get(2).substring(0, 128).toUpperCase()));
      }
    }

    return prefixedHashBundles;

  }

  private AccountUser getAccountUsers(HttpServletRequest request) {
    String base64Credentials = request.getHeader("Authorization");
    String credentials = new String(BaseEncoding.base64()
        .decode(base64Credentials.replaceFirst("^Basic\\s+", "")));

    final String[] values = credentials.split(":", 2);

    return acctUserRepo.findByUsernameAndPassword(values[0],
        HashcodeCreator.getSha512(values[1]));
  }

  private boolean isValidate(HttpServletRequest request) {
    return getAccountUsers(request) != null ? true : false;
  }

  private String getPrefixFromCurrentLoginUser(HttpServletRequest request) {
    return getAccountUsers(request).getInstitutePrefix().getPrefix();
  }

}
